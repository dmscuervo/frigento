package com.soutech.frigento.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.soutech.frigento.model.annotattions.Numeric;

@Entity
@Table(name = "PROMOCION")
public class Promocion implements Serializable {

	private static final long serialVersionUID = -1011711920265345558L;
	
	@Id
    @SequenceGenerator(name = "promocionGen", sequenceName = "SEQ_PROMOCION")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "promocionGen")
    @Column(name = "ID_PROMOCION")
	private Integer id;
	
	@Numeric(regexp = Numeric.decimal_positivo)
    @NotNull
    @Column(name = "DESCUENTO")
	private BigDecimal descuento;
	
	@Numeric(regexp = Numeric.decimal_positivo)
    @NotNull
    @Column(name = "CANTIDAD_MINIMA")
	private Float cantidadMinima;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public BigDecimal getDescuento() {
		return descuento;
	}
	public void setDescuento(BigDecimal descuento) {
		this.descuento = descuento;
	}
	public Float getCantidadMinima() {
		return cantidadMinima;
	}
	public void setCantidadMinima(Float cantidadMinima) {
		this.cantidadMinima = cantidadMinima;
	}
	
}
