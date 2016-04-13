package com.soutech.frigento.web.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.soutech.frigento.model.ProductoCosto;

public class PedidoDTO implements Serializable {

	private static final long serialVersionUID = -1089682630107962323L;

	@NotNull
	private Short estadoId;

	@NotNull
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	private Date fecha;
	
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	private Date fechaEntregar;
	
	@NotNull
	private List<ItemDTO> items;
	
	public Short getEstadoId() {
		return estadoId;
	}

	public void setEstadoId(Short estadoId) {
		this.estadoId = estadoId;
	}

	public Date getFechaEntregar() {
		return fechaEntregar;
	}

	public void setFechaEntregar(Date fechaEntregar) {
		this.fechaEntregar = fechaEntregar;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public List<ItemDTO> getItems() {
		return items;
	}

	public void setItems(List<ItemDTO> items) {
		this.items = items;
	}
	
}
