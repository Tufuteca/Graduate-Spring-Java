package com.tufuteca.grgghotel.service;


import org.springframework.stereotype.Service;

@Service
public class PhoneService {


    public void sendPasswordResetToken(String to, String token) {
        String url = "http://176.197.44.250:8080/change-password?token=" + token;
        System.out.println(to);
        System.out.println(url);
    }
}
