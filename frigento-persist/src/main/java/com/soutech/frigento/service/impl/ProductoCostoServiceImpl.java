package com.soutech.frigento.service.impl;

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
		return productoCostoDao.findActualByProducto(idProd);
	}

	
}