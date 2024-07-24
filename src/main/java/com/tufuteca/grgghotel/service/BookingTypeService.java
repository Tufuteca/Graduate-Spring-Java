package com.tufuteca.grgghotel.service;

import com.tufuteca.grgghotel.model.booking.BookingType;
import com.tufuteca.grgghotel.model.booking.BookingTypePhotos;
import com.tufuteca.grgghotel.repository.booking.BookingTypePhotosRepository;
import com.tufuteca.grgghotel.repository.booking.BookingTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingTypeService {
    @Autowired
    private BookingTypeRepository bookingTypeRepository;
    @Autowired
    private BookingTypePhotosRepository bookingTypePhotosRepository;


    public BookingType findById(Long id) {
    return bookingTypeRepository.findBookingTypeById(id);
    }
    public List<BookingTypePhotos> getPhotosByBookingType(BookingType bookingType) {
        return bookingTypePhotosRepository.findByBookingType(bookingType);
    }

    public void deletePhoto(BookingTypePhotos photo) {
        bookingTypePhotosRepository.delete(photo);
    }

    public List<BookingType> findAll() {
        return bookingTypeRepository.findAll();
    }
}
