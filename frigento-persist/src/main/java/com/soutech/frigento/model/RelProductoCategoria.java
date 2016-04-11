package com.soutech.frigento.model;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "REL_PRODUCTO_CATEGORIA")

public class RelProductoCategoria implements Serializable {

	private static final long serialVersionUID = 1119849523734354664L;

	@Id
    @SequenceGenerator(name = "relProductoCategoriaGen", sequenceName = "SEQ_REL_PROD_CAT")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "relProductoCategoriaGen")
    @Column(name = "ID_REL_PROD_CAT")
    private Integer id;

    @NotNull
    @Column(name = "INCREMENTO")
    private BigDecimal incremento;

    @NotNull
    @Column(name = "F_DESDE")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
    private Date fechaDesde;

    @Column(name = "F_HASTA")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
    private Date fechaHasta;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.REMOVE)
    @JoinColumn(name = "ID_CATEGORIA")
    private Categoria categoria;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PRODUCTO")
    private Producto producto;

    @Transient
    private BigDecimal precioCalculado;
    
    @Transient
    private Integer indiceLista;
    
	public BigDecimal getIncremento() {
        return this.incremento;
    }

	public void setIncremento(BigDecimal incremento) {
        this.incremento = incremento;
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

	public Categoria getCategoria() {
        return this.categoria;
    }

	public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

	public Producto getProducto() {
        return this.producto;
    }

	public void setProducto(Producto producto) {
        this.producto = producto;
    }

	public Integer getId() {
        return this.id;
    }

	public void setId(Integer id) {
        this.id = id;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public BigDecimal getPrecioCalculado() {
		return precioCalculado;
	}

	public void setPrecioCalculado(BigDecimal precioCalculado) {
		this.precioCalculado = precioCalculado;
	}

	public Integer getIndiceLista() {
		return indiceLista;
	}

	public void setIndiceLista(Integer indiceLista) {
		this.indiceLista = indiceLista;
	}
	
}
