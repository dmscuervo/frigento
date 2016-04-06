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

	@Override
	public void saveCategoria(Categoria categoria) {
		categoriaDao.save(categoria);
	}

	@Override
	public Categoria obtenerCategoria(Short id) {
		return categoriaDao.findById(id);
	}

	@Override
	public List<Categoria> obtenerCategorias(String sortFieldName, String sortOrder) {
		return categoriaDao.findAll(sortFieldName, sortOrder);
	}

	@Override
	public void actualizarCategoria(Categoria categoria) {
		categoriaDao.update(categoria);
	}

	@Override
	public void eliminarCategoria(Categoria categoria) {
		categoriaDao.delete(categoria);		
	}
	
}
