package com.soutech.frigento.exception;

public class ControlConcurrenciaExcepcion extends RuntimeException {

	private static final long serialVersionUID = 8791545336083722421L;

	private String path;
	private String keyMessage;
	
	public ControlConcurrenciaExcepcion(String path, String keyMessage){
		this.path = path;
		this.keyMessage = keyMessage;
	}

	public String getPath() {
		return path;
	}

	public String getKeyMessage() {
		return keyMessage;
	}
	
}
