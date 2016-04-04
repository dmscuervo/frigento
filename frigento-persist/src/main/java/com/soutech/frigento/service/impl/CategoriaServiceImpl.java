package com.soutech.frigento.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soutech.frigento.dao.CategoriaDao;
import com.soutech.frigento.model.Categoria;
import com.soutech.frigento.service.CategoriaService;

@Service
public class CategoriaServiceImpl implements CategoriaService {

	@Autowired
    CategoriaDao categoriaDao;

	@Override
	public List<Categoria> obtenerCategorias() {
		return categoriaDao.findAll();
	}

}
