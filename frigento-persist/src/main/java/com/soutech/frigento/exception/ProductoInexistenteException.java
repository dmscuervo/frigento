package com.soutech.frigento.exception;

public class ProductoInexistenteException extends Exception {

	private static final long serialVersionUID = -1175583943887709257L;
	
	private String keyMessage;
	private Object[] args;
	
	public ProductoInexistenteException(String keyMessage, Object[] args){
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
