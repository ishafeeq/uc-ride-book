package com.shafeeq.ucrbook.ucridebook.app.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import com.shafeeq.ucrbook.ucridebook.app.model.Rider;

import lombok.extern.log4j.Log4j2;


@Component
@Log4j2
public class RiderRepository {
    public static final String PASSENGER = "PASSENGER_";
    private Map<String, Rider> userMap = new HashMap<>();
    private AtomicInteger currentId = new AtomicInteger(0);

    public Rider getRider(String riderId){
        Rider user = userMap.get(riderId);
        if(user == null){
            log.info("Rider does not exist for riderId: {}", riderId);
        }
        return user;
    }

    public Rider createRider(Rider rider){
        rider.getUser().setUserId(PASSENGER + Integer.toString(currentId.getAndIncrement()));
        userMap.put(rider.getUser().getUserId(), rider);
        return rider;
    }
}
