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
@Table(name = "PEDIDO")

public class Pedido implements Serializable {

	private static final long serialVersionUID = -715466677393880427L;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_ESTADO")
	private Estado estado;

	@NotNull
	@Column(name = "FECHA")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date fecha;

	@NotNull
	@Column(name = "COSTO")
	@DateTimeFormat(style = "M-")
	private BigDecimal costo;

	@Column(name = "F_ENTREGAR")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date fechaAEntregar;

	@Column(name = "F_ENTREGADO")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date fechaEntregado;

	@Column(name = "F_ANULADO")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date fechaAnulado;

	public Estado getEstado() {
		return this.estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public BigDecimal getCosto() {
		return this.costo;
	}

	public void setCosto(BigDecimal costo) {
		this.costo = costo;
	}

	public Date getFechaAEntregar() {
		return this.fechaAEntregar;
	}

	public void setFechaAEntregar(Date fechaAEntregar) {
		this.fechaAEntregar = fechaAEntregar;
	}

	public Date getFechaEntregado() {
		return this.fechaEntregado;
	}

	public void setFechaEntregado(Date fechaEntregado) {
		this.fechaEntregado = fechaEntregado;
	}

	public Date getFechaAnulado() {
		return this.fechaAnulado;
	}

	public void setFechaAnulado(Date fechaAnulado) {
		this.fechaAnulado = fechaAnulado;
	}

	@Id
	@SequenceGenerator(name = "pedidoGen", sequenceName = "SEQ_PEDIDO")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "pedidoGen")
	@Column(name = "ID_PEDIDO")
	private Integer id;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
