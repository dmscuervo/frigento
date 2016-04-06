package com.soutech.frigento.dao;
import java.util.List;

import com.soutech.frigento.model.Categoria;

public interface CategoriaDao extends IDao<Categoria, Short>{

	List<Categoria> findAll(String sortFieldName, String sortOrder);
}
