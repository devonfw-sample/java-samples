package com.example.demo.test.service;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ServiceBController {
    
    @GetMapping("/serviceB")
    public String getAllServiceB(){
        return "Success\nGET: /api/serviceB";
    }

    @GetMapping("/serviceB/{id}")
    public String getByIdServiceB(@PathVariable Long id){
        return "Success\nGET: /api/serviceB/" + id;
    }

    @PostMapping("/serviceB")
    public String getAllServiceBByPost(){
        return "Success\nPOST: /api/serviceB";
    }

    @PostMapping("/serviceB/{id}")
    public String getByIdServiceBByPost(@PathVariable Long id){
        return "Success\nPOST: /api/serviceB/" + id;
    }
}
