package com.soutech.frigento.web.google;

import java.io.Serializable;

public class Geometry implements Serializable{
	
	private static final long serialVersionUID = -7831176878990153079L;
	
	private Bound bounds;
	private Location location;
	private String location_type;
	private Bound viewport;
	
	public Bound getBounds() {
		return bounds;
	}
	public void setBounds(Bound bounds) {
		this.bounds = bounds;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public String getLocation_type() {
		return location_type;
	}
	public void setLocation_type(String location_type) {
		this.location_type = location_type;
	}
	public Bound getViewport() {
		return viewport;
	}
	public void setViewport(Bound viewport) {
		this.viewport = viewport;
	}
}