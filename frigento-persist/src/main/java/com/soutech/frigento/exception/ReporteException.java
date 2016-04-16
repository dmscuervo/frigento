package com.soutech.frigento.exception;

public class ReporteException extends RuntimeException {

	private static final long serialVersionUID = 3815573279477546800L;
	
	private String keyMessage;
	
	public ReporteException(String keyMessage){
		this.keyMessage = keyMessage;
	}

	public String getKeyMessage() {
		return keyMessage;
	}

}
