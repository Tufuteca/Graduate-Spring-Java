package com.tufuteca.grgghotel.service;

import com.tufuteca.grgghotel.model.room.Service;
import com.tufuteca.grgghotel.repository.room.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

@org.springframework.stereotype.Service
public class ServiceService {
    @Autowired
    private ServiceRepository serviceRepository;

    public List<Service> getAllServices(){
        return serviceRepository.findAll();
    }

    public List<Service> getServicesByEnabled(boolean b) {
        return serviceRepository.findServicesByEnabled(b);
    }
}
