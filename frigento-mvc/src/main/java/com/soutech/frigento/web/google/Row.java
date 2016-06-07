package com.soutech.frigento.web.google;

import java.io.Serializable;

public class Row implements Serializable{

	private static final long serialVersionUID = 3632316765661069427L;
	
	private Element[] elements;

	public Element[] getElements() {
		return elements;
	}

	public void setElements(Element[] elements) {
		this.elements = elements;
	}
	
}
