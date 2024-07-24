package com.tufuteca.grgghotel.service;

import com.tufuteca.grgghotel.model.Photos;
import com.tufuteca.grgghotel.repository.PhotosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhotosService {
    @Autowired
    private PhotosRepository photosRepository;

    public List<Photos> findAll() {
    return photosRepository.findAll();
    }
}
