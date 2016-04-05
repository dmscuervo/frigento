package com.soutech.frigento.service;

import java.util.List;

import com.soutech.frigento.model.Categoria;

public interface CategoriaService {

	public List<Categoria> obtenerCategorias();

	public void saveCategoria(Categoria categoria);

	public Categoria obtenerCategoria(Short id);


}
