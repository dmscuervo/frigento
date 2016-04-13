package com.soutech.frigento.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.soutech.frigento.model.Producto;

public class ItemDTO implements Serializable {

	private static final long serialVersionUID = -6404234372154451460L;

	@NotNull
	private Short cantidad;

	@NotNull
	private Producto producto;
	
	public Short getCantidad() {
		return cantidad;
	}
	public void setCantidad(Short cantidad) {
		this.cantidad = cantidad;
	}
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	
}
