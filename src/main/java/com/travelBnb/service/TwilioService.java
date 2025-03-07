package com.travelBnb.service;

import com.travelBnb.config.TwilioConfig;
import com.twilio.rest.api.v2010.account.Message;

import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    private final TwilioConfig twilioConfig;

    public TwilioService(TwilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
    }
    public String sendSms(String to, String message) {
        try {
            Message sms = Message.creator(
                    new PhoneNumber(to),
                    new PhoneNumber(twilioConfig.getTwilioPhoneNumber()),
                    message).create();
            return sms.getSid();
        } catch (Exception e) {
            // Handle exception appropriately (log it, throw custom exception, etc.)
            e.printStackTrace();
            return null; // or throw new RuntimeException("Failed to send SMS");
        }
    }
}