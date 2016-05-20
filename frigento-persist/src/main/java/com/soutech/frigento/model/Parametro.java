package com.soutech.frigento.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "PARAMETRO")
public class Parametro {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_PARAMETRO")
	private Integer id;
	
	@NotNull
	@Size(max = 40)
	@Column(name = "PARAMETRO", unique = true)
	private String parametro;
	
	@NotNull
    @Size(max = 100)
	@Column(name = "VALOR")
	private String valor;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getParametro() {
		return parametro;
	}
	public void setParametro(String parametro) {
		this.parametro = parametro;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	
}
