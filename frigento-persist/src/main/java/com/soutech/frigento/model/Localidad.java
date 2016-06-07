package com.soutech.frigento.model;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


@Entity
@Table(name = "LOCALIDAD")
public class Localidad implements Serializable {

    private static final long serialVersionUID = 2967879697079462681L;
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_LOCALIDAD")
    private Short id;
	
	@NotNull
	@Size(max = 25)
	@Column(name = "NOMBRE")
	private String nombre;

	public Short getId() {
        return this.id;
    }
	
	public void setId(Short id) {
        this.id = id;
    }
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
