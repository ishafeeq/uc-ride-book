package com.shafeeq.ucrbook.ucridebook.app.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Rider {
    private User user;
    private List<String> rideIds;
    private String currentRideId;
    private Status status;
}
