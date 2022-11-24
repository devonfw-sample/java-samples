package com.example.demo.guest.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/guest/")
public class GuestController {
    
    @GetMapping("/")
    public String getAll(){
        return "ALL GUESTS";
    }

}
