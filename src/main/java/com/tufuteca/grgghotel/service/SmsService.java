package com.tufuteca.grgghotel.service;

import org.springframework.stereotype.Service;

@Service
public class SmsService {


    public void sendSms(String to, String messageText) {
        System.out.println(to+": "+messageText);
    }
}

