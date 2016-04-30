package com.soutech.frigento.web.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.soutech.frigento.model.Producto;

public class PlanillaClienteDTO implements Serializable{

	private static final long serialVersionUID = 7931613162990576379L;
	
	private Date fecha;
	private Short idCategoria;
	private List<String> columns;
	private List<Producto> rows;
	
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Short getIdCategoria() {
		return idCategoria;
	}
	public void setIdCategoria(Short idCategoria) {
		this.idCategoria = idCategoria;
	}
	public List<String> getColumns() {
		return columns;
	}
	public void setColumns(List<String> columns) {
		this.columns = columns;
	}
	public List<Producto> getRows() {
		return rows;
	}
	public void setRows(List<Producto> rows) {
		this.rows = rows;
	}

}
