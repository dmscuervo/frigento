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

//	public static final String VERSION_APP = "VERSION_APP";
//	public static final String NOMBRE_PROVEEDOR = "NOMBRE_PROVEEDOR";
//	public static final String SMTP_GMAIL_PORT = "SMTP_GMAIL_PORT";
//	public static final String SMTP_GMAIL_USER = "info.frigento@gmail.com";
//	public static final String SMTP_GMAIL_PASSWORD = "4l1m3nt0s";
//	public static final String SMTP_GMAIL_REMITENTE = "SMTP_GMAIL_REMITENTE";
//	public static final String SMTP_GMAIL_DESTINATARIOS_PEDIDOS = "SMTP_GMAIL_DESTINATARIOS";
//	public static final String SMTP_GMAIL_DESTINATARIOS_CC_PEDIDOS = "SMTP_GMAIL_DESTINATARIOS_CC";
//	public static final String SMTP_GMAIL_DESTINATARIOS_CC_VENTAS = "SMTP_GMAIL_DESTINATARIOS_CC";
//	
//	public static final String TIME_ZONE_BUENOS_AIRES = "TIME_ZONE_BUENOS_AIRES";
//	
//	public static final String TOLERANCIA_GRAMOS_PROMOCION_VTA = "TOLERANCIA_GRAMOS_PROMOCION_VTA";
	
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
