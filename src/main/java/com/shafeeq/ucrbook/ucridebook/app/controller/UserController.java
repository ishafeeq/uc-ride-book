package com.shafeeq.ucrbook.ucridebook.app.controller;

import com.shafeeq.ucrbook.ucridebook.app.manager.UserManager;
import com.shafeeq.ucrbook.ucridebook.app.model.Driver;
import com.shafeeq.ucrbook.ucridebook.app.model.Rider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

@Controller
public class UserController {


    @Autowired
    private UserManager userManager;

    @GetMapping(value = "/rider",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Rider> getRider(@RequestParam String riderId){
        return ResponseEntity.created(URI.create("/rider")).body(userManager.getRider(riderId));
    }

    @GetMapping(value = "/driver",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Driver> getDriver(@RequestParam String driverId){
        return ResponseEntity.created(URI.create("/driver")).body(userManager.getDriver(driverId));
    }

}
