package com.shafeeq.ucrbook.ucridebook.app.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.shafeeq.ucrbook.ucridebook.app.manager.SearchManager;
import com.shafeeq.ucrbook.ucridebook.app.manager.UserManager;
import com.shafeeq.ucrbook.ucridebook.app.model.DemoRequest;
import com.shafeeq.ucrbook.ucridebook.app.model.DemoResponse;
import com.shafeeq.ucrbook.ucridebook.app.manager.RideManager;
import com.shafeeq.ucrbook.ucridebook.app.model.Driver;
import com.shafeeq.ucrbook.ucridebook.app.model.RideStatus;
import com.shafeeq.ucrbook.ucridebook.app.model.Rider;
import com.shafeeq.ucrbook.ucridebook.app.model.Ride;
import com.shafeeq.ucrbook.ucridebook.app.model.Vehicle;
import com.shafeeq.ucrbook.ucridebook.app.model.VehicleType;
import com.shafeeq.ucrbook.ucridebook.app.util.TestUtil;
import com.uber.h3core.util.GeoCoord;

import lombok.extern.log4j.Log4j2;

@RestController
@ResponseBody
@Controller
@RequestMapping("/")
@Log4j2
public class DemoController {

    @Autowired
    private RideManager rideManager;
    @Autowired
    private SearchManager searchManager;
    @Autowired
    private UserManager userManager;

    @PostMapping(
            value = "/demo",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<DemoResponse> executeDemo(@RequestBody DemoRequest request){
        DemoResponse response = startDemo(request);
        return ResponseEntity.created(URI.create("/demo")).body(response);
    }

    private DemoResponse startDemo(DemoRequest request) {
        log.info("============================ Starting Demo ============================");

        // Step-1 Register Riders or passengers in system
        List<String> riderIds = createRiders();
        log.info("=== Step Summary ===> Registered {} riders/passengers in system\n", riderIds.size());

        // Step-2 Register Riders or drivers in system
        List<String> driverIds = createDrivers();
        log.info("=== Step Summary ===> Registered {} drivers in system\n", driverIds.size());

        // Step-3 Register Riders or drivers in system
        updateDriversLocations(driverIds);
        log.info("=== Step Summary ===> Updated {} drivers location in system\n", driverIds.size());

        // Step-4 Print locations and hexAddr of all drivers
        printLocations(driverIds);
        log.info("=== Step Summary ===> Printed {} drivers location in system\n", driverIds.size());

        // Step-5 Book ride for each rider
        List<Ride> rides = bookRideForRiders(riderIds);
        log.info("=== Step Summary ===> Booked ride for each rider\n");

        // Step-6 Print ongoing rides
        printRides(rides);
        log.info("=== Step Summary ===> Printed ongoing rides\n");

        // Step-7 Print rider and their ongoing rides
        printRiders(riderIds);
        log.info("=== Step Summary ===> Printed Riders with ongoing rides\n");

        // Step-8 Complete RIDE_0 and Fail RIDE_1
        updateRide();
        log.info("=== Step Summary ===> Updated rides as either complete or cancelled\n");

        log.info("=================== Demo Completed ========================");
        return new DemoResponse();
    }



    /*
    ========================================== code below this line is for data creation and executing flow for a set of riders and drivers ========================================
    */
    private void updateRide() {
//        Ride ride1 = rideManager.updateRide("RIDE_0", RideStatus.COMPLETED);
//        Ride ride2 = rideManager.updateRide("RIDE_0", RideStatus.CANCELLED);
//        printRides(Arrays.asList(ride1, ride2));
    }

    private void printRiders(List<String> riderIds) {
        for(int i = 0; i < riderIds.size(); i++){
            Rider rider = userManager.getRider(riderIds.get(i));
            log.info("RiderId: {}, RiderName: {}, currentRideId: {}, status: {}",
                    rider.getUser().getUserId(), rider.getUser().getUserName(), rider.getCurrentRideId(), rider.getStatus());
            if(rider.getCurrentRideId() != null){
                printRides(Arrays.asList(rideManager.getRide(rider.getCurrentRideId())));
            }
        }
    }

    private void printRides(List<Ride> rides) {
        for(int i = 0; i < rides.size(); i++){
            Ride ride = rides.get(i);
            log.info("Ride Details: RideId: {}, Price: {}, distance: {}, pickup: {}, drop: {}",
                    ride.getRideId(), ride.getPrice(), ride.getDistance(), ride.getPickupLocation(), ride.getDropLocation());
        }
    }

    private void printLocations(List<String> driverIds) {
        driverIds.forEach(dId -> {
            Driver driver = userManager.getDriver(dId);
            log.info("location: {}, DriverName: {}, DriverId: {}", searchManager.getDriversLocation(dId), driver.getUser().getUserName(), dId);
        });
    }

    private List<Ride> bookRideForRiders(List<String> riderIds) {
        int ridersCount = riderIds.size();
        double[] lats = {28.6225889, 11.775938,  27.9710297,78.0363866,  14.775938,  37.775938,  27.891516};
        double[] lngs = {77.199821, -113.427951, -123.427951, -113.427951, -131.427951, 78.0661653};

        double[] dlats = {28.5383617, 11.775938,  27.9710297,78.0363866,  14.775938,  37.775938,  27.891516};
        double[] dlngs = {77.0520375, -113.427951, -123.427951, -113.427951, -131.427951, 78.0661653};
        List<Ride> rides = new ArrayList<>();
        for(int i = 0; i < ridersCount; i++){
            Rider rider = userManager.getRider(riderIds.get(i));
            log.info("Booking ride for rider: {}", rider.getUser().getUserName(), rider.getUser().getUserId());
            Ride ride = searchManager.createRide(riderIds.get(i),
                    new GeoCoord(lats[i], lngs[i]), new GeoCoord(dlats[i], dlngs[i]), VehicleType.HATCHBACK);
            if(ride != null){
                log.info("Congratulations! we found driver in your area");
                Driver driver = userManager.getDriver(ride.getDriverId());
                Vehicle vehicle = driver.getVehicle();
                log.info("Vehicle no: {}, VehicleType: {}, VehicleModel: {}, VehicleColor: {}, DriverName: {}, DriverId: {}",
                        vehicle.getRegistrationNo(), vehicle.getVehicleType(), vehicle.getModel(), vehicle.getColor(), driver.getUser().getUserName(), ride.getDriverId());
                log.info("Ride Details: RideId: {}, Price: {}, distance: {}, pickup: {}, drop: {}",
                        ride.getRideId(), ride.getPrice(), ride.getDistance(), ride.getPickupLocation(), ride.getDropLocation());
                rides.add(ride);
            } else {
                log.error("No Driver found for rider: {}", riderIds.get(i));
            }
        }
        return rides;
    }

    private void updateDriversLocations(List<String> driverIds) {
        double[] lats = {28.6220828, 27.775938, 37.775938, 47.735838};
        double[] lngs = {77.2042008, -123.427951, -131.427951, -134.427951};
        for(int i = 0; i < driverIds.size(); i++){
            searchManager.updateLocation(driverIds.get(i), lats[i], lngs[i]);
        }
    }

    private List<String> createRiders() {
        List<String> passengerName = Arrays.asList("shafeeq", "shafeeq2", "shafeeq3", "shafeeq4", "passenger5", "uc-ride");
        return passengerName
                .stream()
                .map(name -> {return userManager.registerRider(TestUtil.createPass(name)).getUser().getUserId();})
                .collect(Collectors.toList());
    }

    private List<String> createDrivers() {
        List<String> driverNames = Arrays.asList("Naresh", "Suresh", "Jitesh", "Ritesh");
        List<String> vehicleColor = Arrays.asList("Blue", "White", "White", "Grey");
        List<String> vehicleRegNo = Arrays.asList("UP-81-ABCD-1111", "UP-81-ABCD-2222", "UP-81-ABCD-3333", "UP-81-ABCD-4444");
        List<VehicleType> vehicleType = Arrays.asList(VehicleType.HATCHBACK, VehicleType.LUXURY, VehicleType.SEDAN, VehicleType.HATCHBACK);
        List<String> vehicleModel = Arrays.asList("Maruti", "Tata", "Baleno", "Nexon");
        List<String> driverIds = new ArrayList<>();
        for(int i = 0; i < driverNames.size(); i++){
            Driver driver = userManager.registerDriver(TestUtil.createDriver(driverNames.get(i)));
            Vehicle vehicle = Vehicle.builder()
                    .vehicleType(vehicleType.get(i))
                    .color(vehicleColor.get(i))
                    .driverId(driver.getUser().getUserId())
                    .model(vehicleModel.get(i))
                    .registrationNo(vehicleRegNo.get(i))
                    .build();
            driver.setVehicle(vehicle);
            driverIds.add(driver.getUser().getUserId());
        }
        return driverIds;
    }
}
