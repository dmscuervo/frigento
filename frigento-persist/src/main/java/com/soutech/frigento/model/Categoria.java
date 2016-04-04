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
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "CATEGORIA")
public class Categoria {

    /**
     */
    @NotNull
    @Size(max = 30)
    @Column(name = "DESCRIPCION")
    @NotEmpty
    private String descripcion;

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@Id
    @SequenceGenerator(name = "categoriaGen", sequenceName = "SEQ_CATEGORIA")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "categoriaGen")
    @Column(name = "ID_CATEGORIA")
    private Short id;

	public Short getId() {
        return this.id;
    }

	public void setId(Short id) {
        this.id = id;
    }

	public String getDescripcion() {
        return this.descripcion;
    }

	public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
