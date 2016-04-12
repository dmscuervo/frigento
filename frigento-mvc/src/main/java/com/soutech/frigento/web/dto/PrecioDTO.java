package com.soutech.frigento.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PrecioDTO implements Serializable{

	private static final long serialVersionUID = 5022765148727212570L;
	
	private BigDecimal costo;
	private Date fechaDesde;
	private BigDecimal[] incrementos;
	
	public BigDecimal getCosto() {
		return costo;
	}
	public void setCosto(BigDecimal costo) {
		this.costo = costo;
	}
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public BigDecimal[] getIncrementos() {
		return incrementos;
	}
	public void setIncrementos(BigDecimal[] incrementos) {
		this.incrementos = incrementos;
	}
	
	
}
