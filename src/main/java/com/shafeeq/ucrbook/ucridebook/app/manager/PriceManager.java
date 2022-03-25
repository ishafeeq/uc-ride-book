package com.shafeeq.ucrbook.ucridebook.app.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.shafeeq.ucrbook.ucridebook.app.model.Driver;
import com.shafeeq.ucrbook.ucridebook.app.model.Ride;
import com.shafeeq.ucrbook.ucridebook.app.model.Rider;
import com.shafeeq.ucrbook.ucridebook.app.model.VehicleType;
import com.sun.tools.javac.util.Pair;
import com.uber.h3core.util.GeoCoord;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class PriceManager {

    private Double minimumPrice;
    private List<Pair<Double, Double>> priceSlabs;
    private Map<VehicleType, Double> vehicleBasedPriceFactorMap;

    @PostConstruct
    public void init(){
        minimumPrice = 50.0;
        priceSlabs = new ArrayList<>();
        priceSlabs.add(Pair.of(0.0, 10.0));
        priceSlabs.add(Pair.of(2.0, 8.0));
        priceSlabs.add(Pair.of(5.0, 5.0));

        vehicleBasedPriceFactorMap = new HashMap<>();
        vehicleBasedPriceFactorMap.put(VehicleType.HATCHBACK, 1.0);
        vehicleBasedPriceFactorMap.put(VehicleType.SEDAN, 1.2);
        vehicleBasedPriceFactorMap.put(VehicleType.LUXURY, 2.5);

    }

    public Double calculateRidePrice(Ride ride, Rider rider, Driver driver){
        GeoCoord pickUp = ride.getPickupLocation();
        GeoCoord drop = ride.getPickupLocation();
        Double rideDist = distance(pickUp.lat, drop.lat, pickUp.lng, drop.lng, 0, 0);
        Double price = 0.0;
        Double upperLimit = 0.0;
        Double lowerLimit = 0.0;
        Double rate = 0.0;
        for(int i = 1; i < priceSlabs.size(); i++){
            rate = priceSlabs.get(i-1).snd;
            upperLimit = priceSlabs.get(i).fst;
            lowerLimit = priceSlabs.get(i-1).fst;
            price = price + rate * (Double.min(rideDist, upperLimit) - lowerLimit);
        }
        price = price + priceSlabs.get(priceSlabs.size()-1).snd * (rideDist - upperLimit - 1);
        price = Double.max(minimumPrice, price);
        ride.setPrice(price);
        ride.setDistance(rideDist);
        log.info("RideId: {}, dist: {}, price: {}", ride.getRideId(), rideDist, price);
        return price;
    }

    /**
     * Below method is copied from : https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude
     *
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     *
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     * @returns Distance in Meters
     */
    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
}
