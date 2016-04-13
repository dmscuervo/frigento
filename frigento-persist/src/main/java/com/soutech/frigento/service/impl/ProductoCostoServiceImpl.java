package com.soutech.frigento.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soutech.frigento.dao.ProductoCostoDao;
import com.soutech.frigento.model.ProductoCosto;
import com.soutech.frigento.service.ProductoCostoService;

@Service
public class ProductoCostoServiceImpl implements ProductoCostoService {

	@Autowired
    ProductoCostoDao productoCostoDao;

	@Override
	public ProductoCosto obtenerActual(Integer idProd) {
		return productoCostoDao.findCostoActual(idProd);
	}

	@Override
	public Date obtenerMinFechaHasta(Integer idProd) {
		return productoCostoDao.getMinFechaHasta(idProd);
	}

	@Override
	public Date obtenerMinFechaDesde(Integer idProd) {
		return productoCostoDao.getMinFechaDesde(idProd);
	}

	@Override
	public List<ProductoCosto> obtenerProductosCosto(String estadoRel, String sortFieldName, String sortOrder) {
		return productoCostoDao.findAll(estadoRel, sortFieldName, sortOrder);
	}

	
}