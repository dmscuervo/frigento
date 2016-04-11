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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "PRODUCTO_COSTO")

public class ProductoCosto implements Serializable {

    private static final long serialVersionUID = 7401897381896795521L;

	@NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PRODUCTO")
    private Producto producto;

    @NotNull
    @Column(name = "COSTO")
    private BigDecimal costo;

    @NotNull
    @Column(name = "F_DESDE")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
    private Date fechaDesde;

    @Column(name = "F_HASTA")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
    private Date fechaHasta;

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@Id
    @SequenceGenerator(name = "productoCostoGen", sequenceName = "SEQ_PRODUCTO_COSTO")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "productoCostoGen")
    @Column(name = "ID_PRODUCTO_COSTO")
    private Integer id;

	public Integer getId() {
        return this.id;
    }

	public void setId(Integer id) {
        this.id = id;
    }

	public Producto getProducto() {
        return this.producto;
    }

	public void setProducto(Producto producto) {
        this.producto = producto;
    }

	public BigDecimal getCosto() {
        return this.costo;
    }

	public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

	public Date getFechaDesde() {
        return this.fechaDesde;
    }

	public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

	public Date getFechaHasta() {
        return this.fechaHasta;
    }

	public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }
}
