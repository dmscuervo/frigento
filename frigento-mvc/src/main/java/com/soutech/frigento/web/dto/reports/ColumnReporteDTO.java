package com.soutech.frigento.web.dto.reports;

public class ColumnReporteDTO {
	
	private String nombre;
	private String property;
	private String className;
	private int ancho;
	private boolean ajustarAncho;
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public int getAncho() {
		return ancho;
	}
	public void setAncho(int ancho) {
		this.ancho = ancho;
	}
	public boolean isAjustarAncho() {
		return ajustarAncho;
	}
	public void setAjustarAncho(boolean ajustarAncho) {
		this.ajustarAncho = ajustarAncho;
	}
	
}
