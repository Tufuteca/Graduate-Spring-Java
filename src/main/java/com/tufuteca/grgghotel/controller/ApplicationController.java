package com.tufuteca.grgghotel.controller;

import com.tufuteca.grgghotel.model.booking.BookingType;
import com.tufuteca.grgghotel.model.booking.BookingTypePhotos;
import com.tufuteca.grgghotel.model.room.RoomPhoto;
import com.tufuteca.grgghotel.model.room.RoomType;
import com.tufuteca.grgghotel.model.room.Service;
import com.tufuteca.grgghotel.model.room.ServiceRoomType;
import com.tufuteca.grgghotel.model.users.Users;
import com.tufuteca.grgghotel.repository.booking.BookingTypePhotosRepository;
import com.tufuteca.grgghotel.repository.room.RoomPhotoRepository;
import com.tufuteca.grgghotel.repository.room.ServiceRoomTypeRepository;
import com.tufuteca.grgghotel.service.BookingTypeService;
import com.tufuteca.grgghotel.service.RoomTypeService;
import com.tufuteca.grgghotel.service.ServiceService;
import com.tufuteca.grgghotel.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Controller
public class ApplicationController {
    private static final String PHOTO_DIR = "static/photos/slider/";
    @Autowired
    private final UserService userService;
    @Autowired
    private final BookingTypeService bookingTypeService;
    @Autowired
    private final BookingTypePhotosRepository bookingTypePhotosRepository;
    @Autowired
    private RoomTypeService roomTypeService;
    @Autowired
    private RoomPhotoRepository roomPhotoRepository;

    public ApplicationController(UserService userService, BookingTypeService bookingTypeService, BookingTypePhotosRepository bookingTypePhotosRepository) {
        this.userService = userService;
        this.bookingTypeService = bookingTypeService;
        this.bookingTypePhotosRepository = bookingTypePhotosRepository;
    }

    @GetMapping("/")
    public String getHomePage(Model model) {
        try {
            List<String> photoNames = loadPhotos();
            model.addAttribute("photoNames", photoNames);
            System.out.println(photoNames);
            return "index";
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }

    private List<String> loadPhotos() throws IOException {
        Resource resource = new ClassPathResource(PHOTO_DIR);
        Path photoPath = Paths.get(resource.getURI());

        return Arrays.stream(photoPath.toFile().list())
                .filter(file -> file.toLowerCase().endsWith(".jpg") || file.toLowerCase().endsWith(".png"))
                .collect(Collectors.toList());
    }




    @GetMapping("/registration")
    public String getRegistrationPage() {
        return "/registration";
    }


    @GetMapping("/dashboard")
    public String getDashboardPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin-panel";
        } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_USER"))) {
            return "redirect:/user-profile";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/booking-types")
    public String getBookingTypesPage(Model model) {
        List<BookingType> bookingTypeList = bookingTypeService.findAll();
        Map<BookingType, List<BookingTypePhotos>> bookingTypePhotoMap = new HashMap<>();
        for (BookingType bookingType : bookingTypeList) {
            bookingTypePhotoMap.put(bookingType, bookingTypePhotosRepository.findByBookingType(bookingType));
        }
        model.addAttribute("bookingTypePhotoMap", bookingTypePhotoMap);
        return "/booking-types";
    }


    @Value("file:upload-dir/photos/bookingTypes")
    private Resource bookingTypesDir;

    @Value("file:upload-dir/photos/rooms")
    private Resource roomsDir;


    @GetMapping("/gallery")
    public String getGallery(Model model) {
        try {
            model.addAttribute("roomPhotos", listFiles(roomsDir, "rooms"));
            model.addAttribute("bookingTypePhotos", listFiles(bookingTypesDir, "bookingTypes"));
        } catch (IOException e) {
            model.addAttribute("error", "Ошибка загрузки фотографий: " + e.getMessage());
        }
        return "gallery";
    }

    private List<String> listFiles(Resource dir, String dirName) throws IOException {
        File directory = dir.getFile(); // Используйте getFile вместо getURI
        try (Stream<Path> paths = Files.walk(directory.toPath(), 1)) {
            return paths.filter(Files::isRegularFile)
                    .map(path -> "/photos/" + dirName + "/" + path.getFileName().toString())
                    .collect(Collectors.toList());
        }
    }




    @Autowired
    private ServiceService serviceService;

    @Autowired
    private ServiceRoomTypeRepository serviceRoomTypeRepository;

    @GetMapping("additionalService")
    public String getAdditionalService(Model model){
        List<Service> serviceList = serviceService.getServicesByEnabled(false);
        model.addAttribute("serviceList",serviceList);
        List<ServiceRoomType> serviceRoomTypes = serviceRoomTypeRepository.findAll();
        Map<Service, List<RoomType>> serviceRoomTypeMap = new LinkedHashMap<>(); // Используем LinkedHashMap для сохранения порядка

        for (ServiceRoomType serviceRoomType : serviceRoomTypes) {
            if (serviceRoomType.getService().getEnabled()) {
                Service service = serviceRoomType.getService();
                RoomType roomType = serviceRoomType.getRoomsType();

                // Если сервис уже есть в карте, добавляем тип номера к списку для этого сервиса
                if (serviceRoomTypeMap.containsKey(service)) {
                    serviceRoomTypeMap.get(service).add(roomType);
                } else { // Иначе, создаем новую запись в карте
                    List<RoomType> roomTypes = new ArrayList<>();
                    roomTypes.add(roomType);
                    serviceRoomTypeMap.put(service, roomTypes);
                }
            }
        }

        model.addAttribute("serviceRoomTypeMap", serviceRoomTypeMap);
        return "additionalService";
    }

    @GetMapping("about-us")
    public String getAboutUs(){
        return "/about-us";
    }
    @GetMapping("contacts")
    public String getContacts(){
        return "/contacts";
    }


}

