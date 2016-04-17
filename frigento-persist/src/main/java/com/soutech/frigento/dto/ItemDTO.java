package com.soutech.frigento.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.soutech.frigento.model.Producto;

public class ItemDTO implements Serializable {

	private static final long serialVersionUID = -6404234372154451460L;

	@NotNull
	private Short cantidad;
	
	@NotNull
	private Producto producto;
	
	private BigDecimal costoCumplir;
	
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
	public BigDecimal getCostoCumplir() {
		return costoCumplir;
	}
	public void setCostoCumplir(BigDecimal costoCumplir) {
		this.costoCumplir = costoCumplir;
	}
	
}
