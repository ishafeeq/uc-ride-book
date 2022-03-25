package com.shafeeq.ucrbook.ucridebook.app.manager;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.shafeeq.ucrbook.ucridebook.app.model.Driver;
import com.shafeeq.ucrbook.ucridebook.app.model.Status;
import com.shafeeq.ucrbook.ucridebook.app.model.Rider;
import com.shafeeq.ucrbook.ucridebook.app.model.Ride;
import com.shafeeq.ucrbook.ucridebook.app.model.RideStatus;
import com.shafeeq.ucrbook.ucridebook.app.repository.HexToDriversRepository;
import com.shafeeq.ucrbook.ucridebook.app.repository.RideRepository;
import com.uber.h3core.H3Core;
import com.uber.h3core.util.GeoCoord;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class SearchManager {

    @Autowired
    private HexToDriversRepository hexToDriversRepository;

    @Autowired
    private UserManager userManager;

    @Autowired
    private RideManager rideManager;

    @Autowired
    private PriceManager priceManager;

    private H3Core h3;
    private int gridResolution = 12;

    @PostConstruct
    public void init(){
        try {
            h3 = H3Core.newInstance();
        } catch (IOException e) {
            log.error("H# instance creation failed");
        }
    }

    public void updateLocation(String driverId, double lat, double lng){
        String hexAddr = h3.geoToH3Address(lat, lng, gridResolution);
        hexToDriversRepository.updateDriverToHexagon(hexAddr, driverId);
    }

    public String getDriversLocation(String driverId){
        String hexHash = hexToDriversRepository.getHexagonalHashForDriverId(driverId);
        GeoCoord cord = h3.h3ToGeo(hexHash);
        return "lat: " + cord.lat + " lng: " + cord.lng + " h3Hash: " + hexHash;
    }

    public Ride createRide(String riderId, GeoCoord pickup, GeoCoord drop) {
        String hexAddr = h3.geoToH3Address(pickup.lat, pickup.lng, gridResolution);
        List<String> allDriverIds = hexToDriversRepository.getDriversListForHexHash(hexAddr);
        List<String> driverIds = getBestDriverId(allDriverIds);
        if(CollectionUtils.isEmpty(driverIds)){
            log.info("No Drivers available in your area. Sorry for inconvinience. HexAddr: {}", hexAddr);
            return null;
        }
        log.info("Found driver for rider's hexAddr: {}", hexAddr);
        Ride rideRequest = Ride.builder()
                .driverId(driverIds.get(0))
                .riderId(riderId)
                .pickupLocation(pickup)
                .dropLocation(drop)
                .rideStatus(RideStatus.SCHEDULED)
                .build();
        Rider rider = userManager.getRider(riderId);
        Driver driver = userManager.getDriver(driverIds.get(0));
        priceManager.calculateRidePrice(rideRequest, rider, driver);

        // 1. Create ride in repository
        Ride ride = rideManager.createRide(rideRequest);

        // 2. Update currentRide of User
        rider.setCurrentRideId(ride.getRideId());
        rider.setStatus(Status.RIDING);

        // 3. Update driver status and current ride
        driver.setCurrentRideId(ride.getRideId());
        driver.setStatus(Status.DRIVING);

        return ride;
    }

    private List<String> getBestDriverId(List<String> driverIds) {
        return driverIds.stream()
            .map(dId -> {
                return userManager.getDriver(dId);
            })
            .filter(driver -> Status.AVAILABLE.name().equals(driver.getStatus().name()))
            // We can sort it on the basis of distance from rider's pickup location
            .map(driver -> driver.getUser().getUserId())
            .collect(Collectors.toList());
    }
}
