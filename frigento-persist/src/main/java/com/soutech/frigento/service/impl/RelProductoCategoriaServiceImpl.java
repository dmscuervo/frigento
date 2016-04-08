package com.soutech.frigento.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soutech.frigento.dao.RelProductoCategoriaDao;
import com.soutech.frigento.model.RelProductoCategoria;
import com.soutech.frigento.service.RelProductoCategoriaService;

@Service
public class RelProductoCategoriaServiceImpl implements RelProductoCategoriaService {

	@Autowired
    RelProductoCategoriaDao relProductoCategoriaDao;

	@Override
	public List<RelProductoCategoria> obtenerProductosCategoria(Short idCat) {
		return relProductoCategoriaDao.findAllByCategoria(idCat);
	}

	
}
