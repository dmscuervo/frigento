package com.soutech.frigento.model;
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
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "VENTA")
public class Venta {

    /**
     */
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ESTADO")
    private Estado estado;

    /**
     */
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_USUARIO")
    private Usuario usuario;

    /**
     */
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date fecha;

    /**
     */
    @NotNull
    @Column(name = "IMPORTE")
    @DateTimeFormat(style = "M-")
    private BigDecimal importe;

    /**
     */
    @Column(name = "F_ENTREGAR")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date fechaAEntregar;

    /**
     */
    @Column(name = "HORARIO")
    @Size(max = 10)
    private String horario;

    /**
     */
    @Column(name = "F_ENTREGADO")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date fechaEntregado;

    /**
     */
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Date getFecha() {
        return this.fecha;
    }

	public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

	public BigDecimal getImporte() {
        return this.importe;
    }

	public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

	public Date getFechaAEntregar() {
        return this.fechaAEntregar;
    }

	public void setFechaAEntregar(Date fechaAEntregar) {
        this.fechaAEntregar = fechaAEntregar;
    }

	public String getHorario() {
        return this.horario;
    }

	public void setHorario(String horario) {
        this.horario = horario;
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
    @SequenceGenerator(name = "ventaGen", sequenceName = "SEQ_VENTA")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ventaGen")
    @Column(name = "ID_VENTA")
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
