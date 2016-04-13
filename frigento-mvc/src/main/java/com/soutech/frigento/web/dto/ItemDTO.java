package com.soutech.frigento.web.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.soutech.frigento.model.ProductoCosto;

public class ItemDTO implements Serializable {

	private static final long serialVersionUID = -6404234372154451460L;

	@NotNull
	private Short cantidad;

	@NotNull
	private ProductoCosto productoCosto;
	
	public Short getCantidad() {
		return cantidad;
	}
	public void setCantidad(Short cantidad) {
		this.cantidad = cantidad;
	}
	public ProductoCosto getProductoCosto() {
		return productoCosto;
	}
	public void setProductoCosto(ProductoCosto productoCosto) {
		this.productoCosto = productoCosto;
	}
}
