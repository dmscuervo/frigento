package com.soutech.frigento.model;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "OBSERVACION")
public class Observacion implements Serializable {

    private static final long serialVersionUID = -1841858516847952484L;

	@Id
    @SequenceGenerator(name = "observacionGen", sequenceName = "SEQ_OBSERVACION")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "observacionGen")
    @Column(name = "ID_OBSERVACION")
    private Integer id;

	@NotNull
    @Column(name = "OBSERVACION")
    private String observacion;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

}
