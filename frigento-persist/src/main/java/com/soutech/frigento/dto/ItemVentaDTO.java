package com.soutech.frigento.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.validation.constraints.NotNull;

import com.soutech.frigento.model.Producto;
import com.soutech.frigento.model.Promocion;

public class ItemVentaDTO implements Serializable {

	private static final long serialVersionUID = -6065901376033227831L;
	
	@NotNull
	private Float cantidad;
	
	@NotNull
	private Producto producto;
	
	private BigDecimal importeVenta;
	private Integer relProductoCategoriaId;
	private Float cantidadModificada;
	
	private Promocion promocion;
	
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

	public Promocion getPromocion() {
		return promocion;
	}

	public void setPromocion(Promocion promocion) {
		this.promocion = promocion;
	}
	
	public Object getEntregaArgMessage(){
		if(cantidad == null || producto == null)	return null;
		BigDecimal stockRounding = new BigDecimal(producto.getStock()).setScale(1, RoundingMode.HALF_DOWN);
		String decimales = String.valueOf(stockRounding.multiply(BigDecimal.TEN).intValueExact());
		String decimal = decimales.substring(decimales.length()-1, decimales.length());
		int decimalInt = Integer.parseInt(decimal);
		if(decimalInt != 0 && decimalInt != 5){
			if(decimalInt > 5){
				stockRounding = stockRounding.subtract(new BigDecimal(decimalInt - 5).divide(BigDecimal.TEN));
			}else if(decimalInt > 0){
				stockRounding = stockRounding.subtract(new BigDecimal(decimalInt).divide(BigDecimal.TEN));
			}
		}
		return stockRounding;
	}
}
