package com.soutech.frigento.dao.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.soutech.frigento.dao.ProductoDao;
import com.soutech.frigento.model.Producto;

@Repository
public class ProductoDaoImpl extends AbstractSpringDao<Producto, Integer> implements ProductoDao {

	@Override
	public Producto findByCodigo(Producto producto) {
		StringBuilder hql = new StringBuilder("from ");
		hql.append(Producto.class.getCanonicalName());
		hql.append(" p where p.codigo = :cod");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("cod", producto.getCodigo());
		return (Producto) query.uniqueResult();
	}

}
