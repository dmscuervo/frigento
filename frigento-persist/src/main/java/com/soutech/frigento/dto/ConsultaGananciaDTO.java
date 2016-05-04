package com.soutech.frigento.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.soutech.frigento.model.Producto;
import com.soutech.frigento.model.Venta;

public class ConsultaGananciaDTO implements Serializable {

	private static final long serialVersionUID = -7764526862871728680L;
	
	private Integer mes;
	private Date fecha;
	private Producto producto;
	private Integer promo;
	private Venta venta;
	private Float cantidad;
	private BigDecimal costo;
	private BigDecimal importeVenta;
	private BigDecimal ganancia;

	public Integer getMes() {
		return mes;
	}
	public void setMes(Integer mes) {
		this.mes = mes;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	public Integer getPromo() {
		return promo;
	}
	public void setPromo(Integer promo) {
		this.promo = promo;
	}
	public Venta getVenta() {
		return venta;
	}
	public void setVenta(Venta venta) {
		this.venta = venta;
	}
	public Float getCantidad() {
		return cantidad;
	}
	public void setCantidad(Float cantidad) {
		this.cantidad = cantidad;
	}
	public BigDecimal getCosto() {
		return costo;
	}
	public void setCosto(BigDecimal costo) {
		this.costo = costo;
	}
	public BigDecimal getImporteVenta() {
		return importeVenta;
	}
	public void setImporteVenta(BigDecimal importeVenta) {
		this.importeVenta = importeVenta;
	}
	public BigDecimal getGanancia() {
		return ganancia;
	}
	public void setGanancia(BigDecimal ganancia) {
		this.ganancia = ganancia;
	}
	
}
