package com.soutech.frigento.service;

import java.util.List;

import com.soutech.frigento.exception.ProductoSinCostoException;
import com.soutech.frigento.model.Venta;

public interface VentaService {

	boolean generarVenta(Venta venta) throws ProductoSinCostoException;
	
	List<Venta> obtenerVentas(Short[] estado, String sortFieldName, String sortOrder);

	Venta obtenerVenta(Integer idVenta);

	boolean actualizarVenta(Venta venta) throws ProductoSinCostoException;

	void anularVenta(Integer ventaId);

	void cumplirVenta(Venta ventaCumplido) throws ProductoSinCostoException;

}
