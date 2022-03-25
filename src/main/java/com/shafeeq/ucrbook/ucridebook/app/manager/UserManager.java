package com.shafeeq.ucrbook.ucridebook.app.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shafeeq.ucrbook.ucridebook.app.model.Driver;
import com.shafeeq.ucrbook.ucridebook.app.model.Rider;
import com.shafeeq.ucrbook.ucridebook.app.repository.DriverRepository;
import com.shafeeq.ucrbook.ucridebook.app.repository.RiderRepository;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class UserManager {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private RiderRepository riderRepository;

    public Driver getDriver(String driverId){
        return driverRepository.getDriver(driverId);
    }

    public Rider getRider(String passId){
        return riderRepository.getRider(passId);
    }
    public Rider registerRider(Rider rider){return riderRepository.createRider(rider);}
    public Driver registerDriver(Driver driver){
        return driverRepository.createDriver(driver);
    }
}
