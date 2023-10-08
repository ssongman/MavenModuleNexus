package com.ssongman.airport.airportapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssongman.airport.airportapi.service.PlaneService;
import com.ssongman.airport.core.model.PlaneDto;

@RestController
@RequestMapping("/api")
public class PlaneController {
	
	@Autowired
	public PlaneService planeService;
	
	@GetMapping("/health")
	public String health() {
		return "Health OK";
	}
	
	@GetMapping("/planes")
	public ResponseEntity<PlaneDto> getPlane() {
		System.out.println("planes");
		PlaneDto planeDto = planeService.getPlane();
		return new ResponseEntity<>(planeDto, HttpStatus.OK);
	}

}
