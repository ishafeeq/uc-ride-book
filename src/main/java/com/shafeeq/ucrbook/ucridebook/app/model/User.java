package com.shafeeq.ucrbook.ucridebook.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

    private String userId;
    private String userName;


}

