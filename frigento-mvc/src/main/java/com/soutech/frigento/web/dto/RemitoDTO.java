package com.soutech.frigento.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class RemitoDTO implements Serializable{

	private static final long serialVersionUID = 3669515785866806887L;
	
	private Float cantidad;
	private String producto;
	private BigDecimal pu;
	private BigDecimal importe;
	
	public Float getCantidad() {
		return cantidad;
	}
	public void setCantidad(Float cantidad) {
		this.cantidad = cantidad;
	}
	public String getProducto() {
		return producto;
	}
	public void setProducto(String producto) {
		this.producto = producto;
	}
	public BigDecimal getPu() {
		return pu;
	}
	public void setPu(BigDecimal pu) {
		this.pu = pu;
	}
	public BigDecimal getImporte() {
		return importe;
	}
	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}
	
}
