package com.soutech.frigento.dao.impl;

import java.util.Collection;
import java.util.Date;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.dao.ProductoCostoDao;
import com.soutech.frigento.model.ProductoCosto;

@Repository
@Transactional(readOnly=true)
public class ProductoCostoDaoImpl extends AbstractSpringDao<ProductoCosto, Integer> implements ProductoCostoDao {

	@Override
	public ProductoCosto findCostoActual(Integer idProd) {
		StringBuilder hql = new StringBuilder("from ");
		hql.append(ProductoCosto.class.getCanonicalName());
		hql.append(" pc where pc.producto.id = :idProd ");
		hql.append(" and pc.fechaHasta is null");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("idProd", idProd);
		return (ProductoCosto) query.uniqueResult();
	}

	@Transactional
	public void delete(ProductoCosto prodCosto) {
		prodCosto.setFechaHasta(new Date());
		saveOrUpdate(prodCosto);
	}
	
	@Transactional
	public void deleteAll(Collection<ProductoCosto> prodCostos) {
		for (ProductoCosto prodCosto : prodCostos)
			//La baja de productoCosto es logica
			delete(prodCosto);
	}

	@Override
	public ProductoCosto findActualByProducto(Integer idProd) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
