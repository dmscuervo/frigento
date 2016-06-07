package com.soutech.frigento.web.google;

import java.io.Serializable;

public class Value implements Serializable{

	private static final long serialVersionUID = -3450123030744245741L;
	
	private String text;
	private String value;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
