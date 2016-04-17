package com.soutech.frigento.service;

import java.util.Date;
import java.util.List;

import com.soutech.frigento.model.ProductoCosto;

public interface ProductoCostoService {

	ProductoCosto obtenerActual(Integer idProd);

	Date obtenerMinFechaHasta(Integer idProd);

	Date obtenerMinFechaDesde(Integer idProd);

	List<ProductoCosto> obtenerProductosCosto(String estadoRel, String sortFieldName, String sortOrder);

	public ProductoCosto obtenerProductoCosto(Integer idProd, Date fecha);

	List<ProductoCosto> obtenerProductosCosto(Date fecha);
}
