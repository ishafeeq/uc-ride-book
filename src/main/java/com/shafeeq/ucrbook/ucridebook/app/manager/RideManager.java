package com.shafeeq.ucrbook.ucridebook.app.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.shafeeq.ucrbook.ucridebook.app.model.Driver;
import com.shafeeq.ucrbook.ucridebook.app.model.Ride;
import com.shafeeq.ucrbook.ucridebook.app.model.RideStatus;
import com.shafeeq.ucrbook.ucridebook.app.model.Rider;
import com.shafeeq.ucrbook.ucridebook.app.model.Status;
import com.shafeeq.ucrbook.ucridebook.app.repository.DriverRepository;
import com.shafeeq.ucrbook.ucridebook.app.repository.RideRepository;
import com.shafeeq.ucrbook.ucridebook.app.repository.RiderRepository;

@Component
public class RideManager {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private UserManager userManager;

    public Ride getRide(String rideId){
        return rideRepository.getRide(rideId);
    }

    public Ride createRide(Ride ride){
        return rideRepository.createRide(ride);
    }

    public Ride updateRide(String rideId, RideStatus rideStatus) {
        // 1. Update ride status
        Ride ride = rideRepository.getRide(rideId);
        if(ride != null && ride.getRideStatus() != null && ride.getRideStatus() == RideStatus.COMPLETED){
            return ride;
        }
        ride.setRideStatus(rideStatus);

        // 2. Update Rider's status and current ride id
        Rider rider = userManager.getRider(ride.getRiderId());
        rider.setCurrentRideId(null);
        List<String> pastRidesOfRider = rider.getRideIds();
        if(CollectionUtils.isEmpty(pastRidesOfRider)){
            pastRidesOfRider = new ArrayList<>();
            rider.setRideIds(pastRidesOfRider);
        }
        pastRidesOfRider.add(rideId);
        rider.setStatus(Status.AVAILABLE);

        // 3. Update driver status and current ride id
        Driver driver = userManager.getDriver(ride.getDriverId());
        driver.setCurrentRideId(null);
        List<String> pastRidesOfDriver = driver.getRideIds();
        if(CollectionUtils.isEmpty(pastRidesOfDriver)){
            pastRidesOfDriver = new ArrayList<>();
            driver.setRideIds(pastRidesOfDriver);
        }
        pastRidesOfDriver.add(rideId);
        driver.setStatus(Status.AVAILABLE);

        return ride;
    }
}
