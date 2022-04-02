package com.shafeeq.ucrbook.ucridebook.app.controller;

import com.shafeeq.ucrbook.ucridebook.app.manager.RideManager;
import com.shafeeq.ucrbook.ucridebook.app.manager.UserManager;
import com.shafeeq.ucrbook.ucridebook.app.model.Ride;
import com.shafeeq.ucrbook.ucridebook.app.model.Rider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

@Controller
public class RideController {


    @Autowired
    private RideManager rideManager;

    @GetMapping(value = "/ride",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Ride> getRider(@RequestParam String rideId){
        return ResponseEntity.created(URI.create("/ride")).body(rideManager.getRide(rideId));
    }
}
