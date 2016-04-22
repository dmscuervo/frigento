package com.soutech.frigento.exception;

public class ProductoSinCategoriaException extends Exception {

	private static final long serialVersionUID = 7156288483183834749L;
	
	private String keyMessage;
	private Object[] args;
	
	public ProductoSinCategoriaException(String keyMessage, Object[] args){
		this.keyMessage = keyMessage;
		this.args = args;
	}

	public String getKeyMessage() {
		return keyMessage;
	}

	public Object[] getArgs() {
		return args;
	}

}
