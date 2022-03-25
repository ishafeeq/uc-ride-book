package com.shafeeq.ucrbook.ucridebook.app.model;

import com.uber.h3core.util.GeoCoord;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class Ride {

    private String rideId;
    private String riderId;
    private String driverId;
    private Integer passengerCount = 1;
    private GeoCoord pickupLocation;
    private GeoCoord dropLocation;
    private RideStatus rideStatus;
    private Double distance;
    private Double price;

}
