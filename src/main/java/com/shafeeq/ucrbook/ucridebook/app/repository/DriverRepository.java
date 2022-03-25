package com.shafeeq.ucrbook.ucridebook.app.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import com.shafeeq.ucrbook.ucridebook.app.model.Driver;
import com.shafeeq.ucrbook.ucridebook.app.model.User;

import lombok.extern.log4j.Log4j2;


@Component
@Log4j2
public class DriverRepository {
    public static final String DRIVER = "DRIVER_";
    private Map<String, Driver> userMap = new HashMap<>();
    private AtomicInteger currentId = new AtomicInteger(0);

    public Driver getDriver(String driverId){
        Driver user = userMap.get(driverId);
        if(user == null){
            log.info("Driver does not exist for driverId: {}", driverId);
        }
        return user;
    }

    public Driver createDriver(Driver driver){
        driver.getUser().setUserId(DRIVER + Integer.toString(currentId.getAndIncrement()));
        userMap.put(driver.getUser().getUserId(), driver);
        return driver;
    }
}
