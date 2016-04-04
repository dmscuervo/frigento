package com.soutech.frigento.dao.impl;

import org.springframework.stereotype.Repository;

import com.soutech.frigento.dao.CategoriaDao;
import com.soutech.frigento.model.Categoria;

@Repository
public class CategoriaDaoImpl extends AbstractSpringDao<Categoria, Short> implements CategoriaDao {

	public CategoriaDaoImpl(){
		System.out.println("");
	}
}
