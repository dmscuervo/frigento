package com.soutech.frigento.exception;

public class UserNameExistenteException extends Exception {

	private static final long serialVersionUID = -7107796083298090645L;
	
	private String keyMessage;
	
	public UserNameExistenteException(String keyMessage){
		this.keyMessage = keyMessage;
	}

	public String getKeyMessage() {
		return keyMessage;
	}

}
