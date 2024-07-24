package com.tufuteca.grgghotel.service;

import com.tufuteca.grgghotel.model.users.Clients;
import com.tufuteca.grgghotel.repository.users.ClientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientsService {
    @Autowired
    private ClientsRepository clientsRepository;

    public void addClient(Clients client) {
        clientsRepository.save(client);
    }
}
