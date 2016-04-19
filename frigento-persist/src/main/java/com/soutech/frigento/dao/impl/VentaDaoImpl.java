package com.soutech.frigento.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.dao.VentaDao;
import com.soutech.frigento.model.Venta;

@Repository
@Transactional(readOnly=true)
public class VentaDaoImpl extends AbstractSpringDao<Venta, Integer> implements VentaDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Venta> findAll(Short[] estado, String sortFieldName, String sortOrder) {
		StringBuilder hql = new StringBuilder("from ");
		hql.append(Venta.class.getCanonicalName());
		hql.append(" p ");
		if(estado != null){
			hql.append("where p.estado.id in (:estadoId) ");
		}
		if(sortFieldName != null){
			hql.append("order by p.");
			hql.append(sortFieldName);
			hql.append(" ");
			hql.append(sortOrder);
		}
		Query query = getSession().createQuery(hql.toString());
		if(estado != null){
			query.setParameterList("estadoId", estado);
		}
		return query.list();
	}
}
