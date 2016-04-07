package com.soutech.frigento.exception;

public class EntityExistException extends Exception {

	private static final long serialVersionUID = 6484904884216904772L;
	private String field;
	private String[] fields;
	private Object[] args;
	
	public EntityExistException(String field){
		this.field = field;
	}
	
	public EntityExistException(String field, Object[]args){
		this.field = field;
		this.args = args;
	}
	
	public EntityExistException(String[] fields){
		this.fields = fields;
	}
	
	public String[] getFields() {
		return fields;
	}
	
	public String getField() {
		return field;
	}

	public Object[] getArgs() {
		return args;
	}
	
}
