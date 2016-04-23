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
	private Integer relProductoCategoriaId;
	private Float cantidadModificada;
	
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

	public Float getCantidadModificada() {
		return cantidadModificada;
	}

	/**
	 * Indica la cantidad a aplicar sobre el stock del producto
	 * @param cantidadModificada
	 */
	public void setCantidadModificada(Float cantidadModificada) {
		this.cantidadModificada = cantidadModificada;
	}

}
