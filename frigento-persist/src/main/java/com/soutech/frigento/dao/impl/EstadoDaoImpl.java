package com.soutech.frigento.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.dao.EstadoDao;
import com.soutech.frigento.enums.TipoEstadoEnum;
import com.soutech.frigento.model.Estado;

@Repository
@Transactional(readOnly=true)
public class EstadoDaoImpl extends AbstractSpringDao<Estado, Short> implements EstadoDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Estado> findAllByTipo(TipoEstadoEnum[] tipoEstadoEnums) {
		StringBuilder hql = new StringBuilder("from ");
		hql.append(Estado.class.getCanonicalName());
		hql.append(" e where e.tipo in (:tipos) ");
		hql.append(" order by e.id");
		Query query = getSession().createQuery(hql.toString());
		query.setParameterList("tipos", tipoEstadoEnums);
		return query.list();
	}

}
