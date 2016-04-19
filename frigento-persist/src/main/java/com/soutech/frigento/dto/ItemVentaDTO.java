package com.soutech.frigento.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.soutech.frigento.model.Producto;

public class ItemVentaDTO implements Serializable {

	private static final long serialVersionUID = -6065901376033227831L;
	
	@NotNull
	private Float cantidad;
	
	@NotNull
	private Producto producto;
	
	private BigDecimal importeVenta;
	private BigDecimal costoVenta;
	private Integer relProductoCategoriaId;
	
	public BigDecimal getImporteVenta() {
		return importeVenta;
	}

	public void setImporteVenta(BigDecimal importeVenta) {
		this.importeVenta = importeVenta;
	}

	public Integer getRelProductoCategoriaId() {
		return relProductoCategoriaId;
	}

	public void setRelProductoCategoriaId(Integer relProductoCategoriaId) {
		this.relProductoCategoriaId = relProductoCategoriaId;
	}

	public BigDecimal getCostoVenta() {
		return costoVenta;
	}

	public void setCostoVenta(BigDecimal costoVenta) {
		this.costoVenta = costoVenta;
	}

	public Float getCantidad() {
		return cantidad;
	}

	public void setCantidad(Float cantidad) {
		this.cantidad = cantidad;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

}
