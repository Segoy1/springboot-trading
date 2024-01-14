package de.segoy.springboottradingweb.service;

import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class BasicAuthEncoder {

    public String encode(String input){
        return Base64.getEncoder().encodeToString(input.getBytes());
    }
}
