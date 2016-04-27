package com.soutech.frigento.service;

import java.util.Date;
import java.util.List;

import com.soutech.frigento.exception.ProductoSinCategoriaException;
import com.soutech.frigento.model.Venta;

public interface VentaService {

	boolean generarVenta(Venta venta) throws ProductoSinCategoriaException;
	
	List<Venta> obtenerVentas(Short[] estado, String sortFieldName, String sortOrder);

	Venta obtenerVenta(Integer idVenta);

	boolean actualizarVenta(Venta venta) throws ProductoSinCategoriaException;

	void anularVenta(Integer ventaId, Date fechaAnulado);

	void cumplirVenta(Integer ventaId, Date fechaEntregado);

}
