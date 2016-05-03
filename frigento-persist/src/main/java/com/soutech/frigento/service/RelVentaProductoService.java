package com.soutech.frigento.service;

import java.util.Date;
import java.util.List;

import com.soutech.frigento.model.RelVentaProducto;

public interface RelVentaProductoService {

	List<RelVentaProducto> obtenerVentas(Integer prodId, Short catId, Date fechaIni, Date fechaFin);
	Date obtenerFechaPrimerVenta(Integer prodId, Short catId, Date fechaIni, Date fechaFin);
	List<RelVentaProducto> obtenerByVenta(Integer idVta, String sortFieldName, String sortOrder);
}
