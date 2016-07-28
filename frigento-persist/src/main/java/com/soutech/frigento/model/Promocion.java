package com.soutech.frigento.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

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
	
	@NotNull
    @Column(name = "F_DESDE")
    //@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
    private Date fechaDesde;

    @Column(name = "F_HASTA")
    //@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
    private Date fechaHasta;
    
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_REL_PROD_CAT")
    private RelProductoCategoria relProdCat;
//    @Transient
//    private Short idRelProdCatSel;
    
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
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	public RelProductoCategoria getRelProdCat() {
		return relProdCat;
	}
	public void setRelProdCat(RelProductoCategoria relProdCat) {
		this.relProdCat = relProdCat;
	}
	
}
