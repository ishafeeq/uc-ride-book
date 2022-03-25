package com.shafeeq.ucrbook.ucridebook;

import java.net.URI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.shafeeq.ucrbook.ucridebook.app.model.DemoRequest;
import com.shafeeq.ucrbook.ucridebook.app.model.DemoResponse;
import com.shafeeq.ucrbook.ucridebook.app.util.TestUtil;

import lombok.extern.log4j.Log4j2;

@SpringBootApplication
public class ApplicationRideBookUC {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationRideBookUC.class, args);
	}

}
