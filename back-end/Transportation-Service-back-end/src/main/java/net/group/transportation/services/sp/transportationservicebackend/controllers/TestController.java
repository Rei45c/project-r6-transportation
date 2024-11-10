package net.group.transportation.services.sp.transportationservicebackend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class TestController {
    
    @GetMapping("/hello")
     public ResponseEntity<String> testHello(){
        return ResponseEntity.ok("Front-end and Back-end connection was succesful");
    }

}
