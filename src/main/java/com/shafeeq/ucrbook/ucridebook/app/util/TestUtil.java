package com.shafeeq.ucrbook.ucridebook.app.util;

import com.shafeeq.ucrbook.ucridebook.app.model.Driver;
import com.shafeeq.ucrbook.ucridebook.app.model.Status;
import com.shafeeq.ucrbook.ucridebook.app.model.Rider;
import com.shafeeq.ucrbook.ucridebook.app.model.User;

public class TestUtil {

    public static Rider createPass(String name){
        User usr = User.builder().userName(name).build();
        return Rider.builder().user(usr).status(Status.AVAILABLE).build();
    }

    public static Driver createDriver(String name){
        User usr = User.builder().userName(name).build();
        return Driver.builder().user(usr).status(Status.AVAILABLE).build();
    }
}
