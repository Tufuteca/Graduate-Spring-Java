package com.tufuteca.grgghotel.controller;


import com.tufuteca.grgghotel.model.Status;
import com.tufuteca.grgghotel.model.booking.Booking;
import com.tufuteca.grgghotel.model.booking.BookingType;
import com.tufuteca.grgghotel.model.room.RoomType;
import com.tufuteca.grgghotel.model.room.Rooms;
import com.tufuteca.grgghotel.model.users.Users;
import com.tufuteca.grgghotel.repository.StatusRepository;
import com.tufuteca.grgghotel.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


@Controller
public class RoomBookingController {

    @Autowired
    private RoomTypeService roomTypeService;

    @Autowired
    private RoomsService roomsService;

    @Autowired
    private BookingTypeService bookingTypeService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private UserService userService;
    @Autowired
    private StatusRepository statusRepository;

    @GetMapping("/room-booking")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String showBookingForm(@RequestParam("roomType") Long roomTypeId, Model model) {
        RoomType roomType = roomTypeService.findById(roomTypeId);
        model.addAttribute("roomType", roomType);

        List<BookingType> bookingTypes = bookingTypeService.findAll();
        model.addAttribute("bookingTypes", bookingTypes);

        // Получаем все комнаты данного типа
        List<Rooms> rooms = roomsService.findByRoomType(roomType);

        // Получаем все брони для этих комнат
        List<Booking> bookings = new ArrayList<>();
        List<Booking> existingBookings = new ArrayList<>();
        for (Rooms room : rooms) {
            for(Booking b: bookingService.findBookingsByRoom(room)) {
                if(b.getStatus().getId()!=5 && b.getStatus().getId()!=4) {
                    bookings.add(b);
                    existingBookings.addAll(bookings);
                }
            }
        }

        // Предполагаем, что у вас есть список всех комнат определенного типа
        List<Rooms> allRoomsOfType = roomsService.findByRoomType(roomType);

        // Создаем карту для хранения заблокированных дат для каждой комнаты
        Map<Rooms, List<LocalDate>> roomBlockedDates = new HashMap<>();

        // Заполняем карту данными о бронированиях
        for (Booking booking : existingBookings) {
            LocalDate startDate = booking.getCheckInDate();
            LocalDate endDate = booking.getDepartureDate();
            Rooms bookedRoom = booking.getRooms(); // Предполагаем, что у Booking есть метод getRooms()

            // Добавляем диапазон дат бронирования в карту для соответствующей комнаты
            List<LocalDate> dates = roomBlockedDates.computeIfAbsent(bookedRoom, k -> new ArrayList<>());
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                dates.add(date);
            }
        }

        // Используем функцию для определения полностью забронированных периодов
        List<String> fullyBookedPeriods = findFullyBookedPeriods(allRoomsOfType, roomBlockedDates);

        // В вашем контроллере
        List<Map<String, String>> disabledDates = fullyBookedPeriods.stream()
                .map(period -> {
                    String[] dates = period.split(" - ");
                    Map<String, String> dateMap = new HashMap<>();
                    dateMap.put("start", dates[0]);
                    dateMap.put("end", dates[1]);
                    return dateMap;
                })
                .collect(Collectors.toList());

        model.addAttribute("disabledDates", disabledDates);


        model.addAttribute("existingBookings", existingBookings);

        return "room-booking";
    }

    @PostMapping("/handle-booking")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String handleBooking(
            @RequestParam("roomTypeId") Long roomTypeId,
            @RequestParam("bookingTypeId") int bookingTypeId,
            @RequestParam("checkInDate") String checkInDate,
            @RequestParam("departureDate") String departureDate,
            @RequestParam("description") String description,
            Model model,
            RedirectAttributes redirectAttributes) {

        LocalDate checkIn = LocalDate.parse(checkInDate);
        LocalDate departure = LocalDate.parse(departureDate);

        // Получаем все комнаты указанного типа
        RoomType roomType = roomTypeService.findById(roomTypeId);
        List<Rooms> allRoomsOfType = roomsService.findByRoomType(roomType);

        // Получаем все брони для этих комнат
        List<Booking> existingBookings = new ArrayList<>();
        for (Rooms room : allRoomsOfType) {
            List<Booking> bookings = bookingService.findBookingsByRoom(room);
            existingBookings.addAll(bookings.stream()
                    .filter(b -> b.getStatus().getId() != 5 && b.getStatus().getId() != 4)
                    .collect(Collectors.toList()));
        }

        // Создаем карту для хранения заблокированных дат для каждой комнаты
        Map<Rooms, List<LocalDate>> roomBlockedDates = new HashMap<>();
        for (Booking booking : existingBookings) {
            LocalDate startDate = booking.getCheckInDate();
            LocalDate endDate = booking.getDepartureDate();
            Rooms bookedRoom = booking.getRooms();

            List<LocalDate> dates = roomBlockedDates.computeIfAbsent(bookedRoom, k -> new ArrayList<>());
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                dates.add(date);
            }
        }

        // Проверяем доступность комнат
        Optional<Rooms> availableRoom = allRoomsOfType.stream()
                .filter(room -> {
                    List<LocalDate> blockedDates = roomBlockedDates.getOrDefault(room, Collections.emptyList());
                    for (LocalDate date = checkIn; !date.isAfter(departure); date = date.plusDays(1)) {
                        if (blockedDates.contains(date)) {
                            return false;
                        }
                    }
                    return true;
                })
                .findFirst();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = userService.findByUsername(authentication.getName());
        if (availableRoom.isPresent()) {

            List<Booking> bookings = bookingService.findByUsers(user);
            BookingType bookingType = bookingTypeService.findById(Long.parseLong("" + bookingTypeId));

            Booking booking = new Booking();
            booking.setRooms(availableRoom.get());
            booking.setCheckInDate(checkIn);
            booking.setDepartureDate(departure);
            booking.setDescription(description);
            booking.setBookingType(bookingType);
            // Вычисление количества дней проживания
            float amount = ChronoUnit.DAYS.between(checkIn, departure);
            amount = amount * roomType.getPrice();
            float discount = bookingType.getDiscount();

            amount = amount * ((100 - discount) / 100);
            booking.setAmount(amount);

            booking.setBookingDate(LocalDate.now());
            System.out.println(amount);
            Status status = statusRepository.findStatusById(6L);
            booking.setUsers(user);
            booking.setStatus(status);
            bookingService.save(booking);
            redirectAttributes.addFlashAttribute("successBooking", "Бронирование успешно!");
        } else {
            // Если нет доступных комнат, возвращаем сообщение об ошибке
            redirectAttributes.addFlashAttribute("errorBooking", "Нет доступных комнат в указанные даты.");
        }

        return "redirect:/user-profile";
    }



    // Функция для определения дат, когда все комнаты заняты
    public static List<String> findFullyBookedPeriods(List<Rooms> allRooms, Map<Rooms, List<LocalDate>> blockedDates) {
        // Сортируем даты и удаляем дубликаты
        Set<LocalDate> allBlockedDates = new TreeSet<>();
        blockedDates.values().forEach(allBlockedDates::addAll);

        List<String> fullyBookedPeriods = new ArrayList<>();
        LocalDate periodStart = null;
        LocalDate periodEnd = null;

        for (LocalDate date : allBlockedDates) {
            final LocalDate currentDate = date;
            // Проверяем, заняты ли все комнаты в эту дату
            boolean allRoomsBooked = allRooms.stream().allMatch(room -> blockedDates.getOrDefault(room, Collections.emptyList()).contains(currentDate));

            if (allRoomsBooked) {
                // Если начало периода еще не установлено, устанавливаем его
                if (periodStart == null) {
                    periodStart = date;
                }
                periodEnd = date;
            } else if (periodStart != null) {
                // Если был начат период и мы нашли свободную дату, завершаем период
                fullyBookedPeriods.add(periodStart + " - " + periodEnd);
                periodStart = null;
                periodEnd = null;
            }
        }

        // Добавляем последний период, если он не был закрыт
        if (periodStart != null) {
            fullyBookedPeriods.add(periodStart + " - " + periodEnd);
        }

        return fullyBookedPeriods;
    }






}
