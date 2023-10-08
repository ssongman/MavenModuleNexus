package com.ssongman.airport.core.model;

public class PlaneDto {
	String name;
	int enginesCount;
	int backEnginesCount;
	int sideEnginesCount;
	int frontEnginesCount;
	int front2EnginesCount;
	
	public PlaneDto(String name, int enginesCount, int backEnginesCount, int sideEnginesCount, int frontEnginesCount, int front2EnginesCount) {
		super();
		this.name = name;
		this.enginesCount = enginesCount;
		this.backEnginesCount = backEnginesCount;
		this.sideEnginesCount = sideEnginesCount;
		this.frontEnginesCount = frontEnginesCount;
		this.front2EnginesCount = front2EnginesCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getEnginesCount() {
		return enginesCount;
	}

	public void setEnginesCount(int enginesCount) {
		this.enginesCount = enginesCount;
	}

	public int getBackEnginesCount() {
		return backEnginesCount;
	}

	public void setBackEnginesCount(int backEnginesCount) {
		this.backEnginesCount = backEnginesCount;
	}

	public int getSideEnginesCount() {
		return sideEnginesCount;
	}

	public void setSideEnginesCount(int sideEnginesCount) {
		this.sideEnginesCount = sideEnginesCount;
	}

	public int getFrontEnginesCount() {
		return frontEnginesCount;
	}

	public void setFrontEnginesCount(int frontEnginesCount) {
		this.frontEnginesCount = frontEnginesCount;
	}

	public int getFront2EnginesCount() {
		return front2EnginesCount;
	}

	public void setFront2EnginesCount(int front2EnginesCount) {
		this.front2EnginesCount = front2EnginesCount;
	}
	
	
	
	
}
