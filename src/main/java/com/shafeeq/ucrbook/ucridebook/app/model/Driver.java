package com.shafeeq.ucrbook.ucridebook.app.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Driver {

    private User user;
    private Vehicle vehicle;
    private Status status;
    private List<String> rideIds;
    private String currentRideId;
}
