package com.soutech.frigento.web.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class GenericController {
	
	@Autowired
	private MessageSource messageSource;
	
	protected String getMessage(String key){
		return messageSource.getMessage(key, null, Locale.getDefault());
	}
	
	protected String getMessage(String key, Object arg){
		return messageSource.getMessage(key, new Object[]{arg}, Locale.getDefault());
	}
    
	protected String getMessage(String key, Object[] args){
		return messageSource.getMessage(key, args, Locale.getDefault());
	}
}
