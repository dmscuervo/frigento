package com.soutech.frigento.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.dao.ProductoDao;
import com.soutech.frigento.service.ProductoService;

@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {	

	@Autowired
    ProductoDao productoDao;

}