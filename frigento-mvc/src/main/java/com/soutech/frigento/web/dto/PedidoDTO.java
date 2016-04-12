package com.soutech.frigento.web.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.soutech.frigento.model.annotattions.Numeric;

public class PedidoDTO implements Serializable {

	private static final long serialVersionUID = -1089682630107962323L;

	@NotNull
	private Short estadoId;

	@NotNull
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	private Date fecha;
	
	@NotNull
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	private Date fechaEntregar;
	
	@NotNull
	private List<Item> items;
	
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

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	public class Item{
		
		@NotNull
		private Float cantidad;
		@Numeric(regexp = Numeric.decimal_positivo)
		private String producto;
		
		public Float getCantidad() {
			return cantidad;
		}
		public void setCantidad(Float cantidad) {
			this.cantidad = cantidad;
		}
		public String getProducto() {
			return producto;
		}
		public void setProducto(String producto) {
			this.producto = producto;
		}
		
	}
}
