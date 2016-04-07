package com.soutech.frigento.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.dao.RelProductoCategoriaDao;
import com.soutech.frigento.model.RelProductoCategoria;

@Repository
@Transactional(readOnly=true)
public class RelProductoCategoriaDaoImpl extends AbstractSpringDao<RelProductoCategoria, Integer> implements RelProductoCategoriaDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<RelProductoCategoria> findAllByCategoria(Short idCat) {
		StringBuilder hql = new StringBuilder("from ");
		hql.append(RelProductoCategoria.class.getCanonicalName());
		hql.append(" r where r.categoria.id = :catId");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("catId", idCat);
		return query.list();
	}

}
