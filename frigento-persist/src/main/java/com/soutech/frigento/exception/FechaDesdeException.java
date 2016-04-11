package com.soutech.frigento.exception;

public class FechaDesdeException extends Exception {

	private static final long serialVersionUID = -7799272585201776733L;
	
	private String keyMessage;
	private Object[] args;
	
	public FechaDesdeException(String keyMessage, Object[] args){
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
