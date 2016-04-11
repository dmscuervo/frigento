package com.soutech.frigento.service;

import java.util.Date;

import com.soutech.frigento.model.ProductoCosto;

public interface ProductoCostoService {

	ProductoCosto obtenerActual(Integer idProd);

	Date obtenerMinFechaHasta(Integer idProd);

	Date obtenerMinFechaDesde(Integer idProd);

}
