package com.soutech.frigento.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.dao.CategoriaDao;
import com.soutech.frigento.model.Categoria;

@Repository
@Transactional(readOnly=true)
public class CategoriaDaoImpl extends AbstractSpringDao<Categoria, Short> implements CategoriaDao {

	@Override
	public List<Categoria> findAll(String sortFieldName, String sortOrder) {
		StringBuilder query = new StringBuilder("from ");
		query.append(Categoria.class.getCanonicalName());
		query.append(" c ");
		if(sortFieldName != null){
			query.append("order by ");
			query.append(sortFieldName);
			query.append(" ");
			query.append(sortOrder);
		}
		return findQuery(query.toString());
	}

	
}
