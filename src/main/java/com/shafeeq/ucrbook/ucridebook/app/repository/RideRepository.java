package com.shafeeq.ucrbook.ucridebook.app.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import com.shafeeq.ucrbook.ucridebook.app.model.Ride;

import lombok.extern.log4j.Log4j2;


@Component
@Log4j2
public class RideRepository {

    public static final String RIDE = "RIDE_";
    private Map<String, Ride> rideMap = new HashMap<>();
    private AtomicInteger currentId = new AtomicInteger(0);

    public Ride getRide(String rideId){
        Ride ride = rideMap.get(rideId);
        if(ride == null){
            log.info("Ride does not exist for rideId: {}", rideId);
        }
        return ride;
    }

    public Ride createRide(Ride ride){
        ride.setRideId(RIDE + Integer.toString(currentId.getAndIncrement()));
        rideMap.put(ride.getRideId(), ride);
        return ride;
    }
}
