package com.shafeeq.ucrbook.ucridebook.app.model;

import com.uber.h3core.util.GeoCoord;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class Vehicle {

    private String color;
    private String model;
    private String registrationNo;
    private String driverId;
    private VehicleType vehicleType;


    public Integer calculatePrice(GeoCoord start, GeoCoord end){
        //Write logic for price calculation based on Vehicle Type
        return 0;
    }
}
