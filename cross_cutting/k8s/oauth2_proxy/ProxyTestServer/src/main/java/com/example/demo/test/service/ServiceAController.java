package com.example.demo.test.service;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ServiceAController {
    
    @GetMapping("/serviceA")
    public String getAllServiceA(){
        return "Success\nGET: /api/serviceA";
    }

    @GetMapping("/serviceA/{id}")
    public String getByIdServiceA(@PathVariable Long id){
        return "Success\nGET: /api/serviceA/" + id;
    }

    @PostMapping("/serviceA")
    public String getAllServiceAByPost(){
        return "Success\nPOST: /api/serviceA";
    }

    @PostMapping("/serviceA/{id}")
    public String getByIdServiceAByPost(@PathVariable Long id){
        return "Success\nPOST: /api/serviceA/" + id;
    }
}
