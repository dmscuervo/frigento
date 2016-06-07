package com.soutech.frigento.web.google;

import java.io.Serializable;

public class Result implements Serializable{
	
	private static final long serialVersionUID = 764141163938641962L;
	
	private Address[] address_components;
	private String formatted_address;
	private Geometry geometry;
	private Boolean partial_match;
	private String place_id;
	private String[] types;
	
	public Address[] getAddress_components() {
		return address_components;
	}
	public void setAddress_components(Address[] address_components) {
		this.address_components = address_components;
	}
	public String getFormatted_address() {
		return formatted_address;
	}
	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}
	
	public Boolean getPartial_match() {
		return partial_match;
	}
	public void setPartial_match(Boolean partial_match) {
		this.partial_match = partial_match;
	}
	public Geometry getGeometry() {
		return geometry;
	}
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
	public String getPlace_id() {
		return place_id;
	}
	public void setPlace_id(String place_id) {
		this.place_id = place_id;
	}
	public String[] getTypes() {
		return types;
	}
	public void setTypes(String[] types) {
		this.types = types;
	}
}
