package com.soutech.frigento.web.google;

import java.io.Serializable;

public class Address implements Serializable{
	
	private static final long serialVersionUID = 8219760523260278495L;
	
	private String long_name;
	private String short_name;
	private String[] types;
	
	public String getLong_name() {
		return long_name;
	}
	public void setLong_name(String long_name) {
		this.long_name = long_name;
	}
	public String getShort_name() {
		return short_name;
	}
	public void setShort_name(String short_name) {
		this.short_name = short_name;
	}
	public String[] getTypes() {
		return types;
	}
	public void setTypes(String[] types) {
		this.types = types;
	}
}
