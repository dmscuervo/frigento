package com.soutech.frigento.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "USUARIO")

public class Cliente {

    /**
     */
    @NotNull
    @Column(name = "NOMBRE")
    @Size(max = 20)
    private String nombre;

    /**
     */
    @Column(name = "APPELLIDO")
    @Size(max = 20)
    private String apellido;

    /**
     */
    @Column(name = "TELEFONO")
    @Size(max = 15)
    private String telefono;

    /**
     */
    @Column(name = "CELULAR")
    @Size(max = 15)
    private String celular;

    /**
     */
    @Column(name = "CALLE")
    @Size(max = 30)
    private String calle;

    /**
     */
    @Column(name = "ALTURA")
    private Short altura;

    /**
     */
    @Column(name = "DEPTO")
    @Size(max = 10)
    private String depto;

    /**
     */
    @NotNull
    @Column(name = "ES_ADMIN")
    private Boolean esAdmin;

	@Id
    @SequenceGenerator(name = "clienteGen", sequenceName = "SEQ_USUARIO")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "clienteGen")
    @Column(name = "ID_USUARIO")
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

	public String getNombre() {
        return this.nombre;
    }

	public void setNombre(String nombre) {
        this.nombre = nombre;
    }

	public String getApellido() {
        return this.apellido;
    }

	public void setApellido(String apellido) {
        this.apellido = apellido;
    }

	public String getTelefono() {
        return this.telefono;
    }

	public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

	public String getCelular() {
        return this.celular;
    }

	public void setCelular(String celular) {
        this.celular = celular;
    }

	public String getCalle() {
        return this.calle;
    }

	public void setCalle(String calle) {
        this.calle = calle;
    }

	public Short getAltura() {
        return this.altura;
    }

	public void setAltura(Short altura) {
        this.altura = altura;
    }

	public String getDepto() {
        return this.depto;
    }

	public void setDepto(String depto) {
        this.depto = depto;
    }

	public Boolean getEsAdmin() {
        return this.esAdmin;
    }

	public void setEsAdmin(Boolean esAdmin) {
        this.esAdmin = esAdmin;
    }
}
