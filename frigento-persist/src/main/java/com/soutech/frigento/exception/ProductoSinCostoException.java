package com.soutech.frigento.exception;

public class ProductoSinCostoException extends Exception {

	private static final long serialVersionUID = 6165178616532666658L;
	
	private String keyMessage;
	private Object[] args;
	
	public ProductoSinCostoException(String keyMessage, Object[] args){
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
