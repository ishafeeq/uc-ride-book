package com.shafeeq.ucrbook.ucridebook.app.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Driver {

    private User user;
    private Vehicle vehicle;
    private Status status;
    private String currentRideId;
}
