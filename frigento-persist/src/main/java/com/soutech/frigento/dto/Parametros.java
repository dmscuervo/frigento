package com.soutech.frigento.dto;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class Parametros {
	
	public static final String VERSION_APP = "VERSION_APP";
	public static final String NOMBRE_PROVEEDOR = "NOMBRE_PROVEEDOR";
	public static final String SMTP_GMAIL_PORT = "SMTP_GMAIL_PORT";
	public static final String SMTP_GMAIL_PASSWORD = "SMTP_GMAIL_PASSWORD";
	public static final String SMTP_GMAIL_REMITENTE = "SMTP_GMAIL_REMITENTE";
	public static final String SMTP_GMAIL_DESTINATARIOS_PEDIDOS = "SMTP_GMAIL_DESTINATARIOS_PEDIDOS";
	public static final String SMTP_GMAIL_DESTINATARIOS_CC_PEDIDOS = "SMTP_GMAIL_DESTINATARIOS_CC_PEDIDOS";
	public static final String SMTP_GMAIL_DESTINATARIOS_CC_VENTAS = "SMTP_GMAIL_DESTINATARIOS_CC_VENTAS";
	
	public static final String TIME_ZONE_BUENOS_AIRES = "TIME_ZONE_BUENOS_AIRES";
	
	public static final String TOLERANCIA_GRAMOS_PROMOCION_VTA = "TOLERANCIA_GRAMOS_PROMOCION_VTA";

	private static Map<String, String> parametros;
	
	public void setParametros(Map<String, String> parametros) {
		Parametros.parametros = parametros;
	}

	public static String getValor(String parametro){
		return parametros.get(parametro);
	}
}
