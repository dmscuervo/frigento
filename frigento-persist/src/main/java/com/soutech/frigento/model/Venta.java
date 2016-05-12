package com.soutech.frigento.model;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import com.soutech.frigento.dto.ItemVentaDTO;


@Entity
@Table(name = "VENTA")
public class Venta implements Serializable {

    private static final long serialVersionUID = 4671255183360631983L;
    
    @Id
    @SequenceGenerator(name = "ventaGen", sequenceName = "SEQ_VENTA")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ventaGen")
    @Column(name = "ID_VENTA")
    private Integer id;

	@NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ESTADO")
    private Estado estado;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_USUARIO")
    private Usuario usuario;

    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date fecha;

    @NotNull
    @Column(name = "IMPORTE")
    private BigDecimal importe;
    
    @NotNull
    @Column(name = "INCREMENTO_IVA")
    private BigDecimal incrementoIva;

    @Column(name = "F_ENTREGAR")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date fechaAEntregar;

    @Column(name = "HORARIO")
    @Size(max = 10)
    private String horario;

    @Column(name = "F_ENTREGADO")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date fechaEntregado;

    @Column(name = "F_ANULADO")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date fechaAnulado;

    @NotNull
    @Column(name = "VERSION")
    private Short version;
    
    @Transient
    private Boolean conIva;

    @Valid
	@Transient
	private List<ItemVentaDTO> items;
	
	@Transient
	private Boolean envioMail = Boolean.FALSE;

    
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

	public Integer getId() {
        return this.id;
    }

	public void setId(Integer id) {
        this.id = id;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public List<ItemVentaDTO> getItems() {
		return items;
	}

	public void setItems(List<ItemVentaDTO> items) {
		this.items = items;
	}

	public Boolean getEnvioMail() {
		return envioMail;
	}

	public void setEnvioMail(Boolean envioMail) {
		this.envioMail = envioMail;
	}

	public Short getVersion() {
		return version;
	}

	public void setVersion(Short version) {
		this.version = version;
	}

	public Boolean getConIva() {
		return conIva;
	}

	public void setConIva(Boolean conIva) {
		this.conIva = conIva;
	}

	public BigDecimal getIncrementoIva() {
		return incrementoIva;
	}

	public void setIncrementoIva(BigDecimal incrementoIva) {
		this.incrementoIva = incrementoIva;
	}

}
