package com.soutech.frigento.web.google;

import java.io.Serializable;

public class Element implements Serializable{
	
	private static final long serialVersionUID = 6143796975887918602L;
	
	private Value distance;
	private Value duration;
	private String status;
	
	public Value getDistance() {
		return distance;
	}
	public void setDistance(Value distance) {
		this.distance = distance;
	}
	public Value getDuration() {
		return duration;
	}
	public void setDuration(Value duration) {
		this.duration = duration;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
