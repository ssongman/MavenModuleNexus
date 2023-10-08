package com.ssongman.airport.airportapi.service;

import org.springframework.stereotype.Service;

import com.ssongman.airport.core.model.PlaneDto;

@Service
public class PlaneService {
	
	public String health() {
		return "OK";
	}
	
	public PlaneDto getPlane() {
		return new PlaneDto("korean air", 4, 2, 1, 1, 1);
	}

}
