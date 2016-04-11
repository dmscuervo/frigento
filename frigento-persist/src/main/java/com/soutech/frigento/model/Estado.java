package com.soutech.frigento.model;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.soutech.frigento.enums.TipoEstadoEnum;


@Entity
@Table(name = "ESTADO")
public class Estado implements Serializable {

    private static final long serialVersionUID = -7234561460573812053L;
	
    @NotNull
    @Size(max = 15)
    private String descripcion;

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getDescripcion() {
        return this.descripcion;
    }

	public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_ESTADO")
    private Short id;

	public Short getId() {
        return this.id;
    }
	
	@Enumerated(EnumType.STRING)
	@Column(name="tipo")
	private TipoEstadoEnum tipo;

	public void setId(Short id) {
        this.id = id;
    }

	public TipoEstadoEnum getTipo() {
		return tipo;
	}

	public void setTipo(TipoEstadoEnum tipo) {
		this.tipo = tipo;
	}
	
	
}
