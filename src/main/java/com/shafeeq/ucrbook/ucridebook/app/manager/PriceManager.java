package com.shafeeq.ucrbook.ucridebook.app.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.shafeeq.ucrbook.ucridebook.app.util.DistanceUtil;
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

    public Double calculateRidePrice(Ride ride, VehicleType vehicleType){
        GeoCoord pickUp = ride.getPickupLocation();
        GeoCoord drop = ride.getDropLocation();
//        Double rideDist = DistanceUtil.distance(pickUp.lat, drop.lat, pickUp.lng, drop.lng, 0, 0);
        Double rideDist = DistanceUtil.haverSineDistance(pickUp.lat, drop.lat, pickUp.lng, drop.lng);

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
        price = Double.max(minimumPrice, price) * vehicleBasedPriceFactorMap.get(vehicleType);
        ride.setPrice(price);
        ride.setDistance(rideDist);
        log.info("RideId: {}, dist: {}, price: {}", ride.getRideId(), rideDist, price);
        return price;
    }

}
