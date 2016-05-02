package com.soutech.frigento.web.dto.reports;

import java.io.Serializable;

import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;

public class ColumnReporteDTO implements Serializable{
	
	private static final long serialVersionUID = -1433683483297484407L;
	
	private String nombre;
	private String property;
	private String className;
	private int ancho;
	private boolean ajustarAncho;
	private String pattern;
	private HorizontalAlign alineacionHorizontal = HorizontalAlign.CENTER;
	
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
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public HorizontalAlign getAlineacionHorizontal() {
		return alineacionHorizontal;
	}
	public void setAlineacionHorizontal(HorizontalAlign alineacionHorizontal) {
		this.alineacionHorizontal = alineacionHorizontal;
	}
	public Style generarStyle(Style base){
		if(alineacionHorizontal == null)	return base;
		
		Style style;
		try {
			style = (Style) base.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
		style.setHorizontalAlign(alineacionHorizontal);
		return style;
	}
}
