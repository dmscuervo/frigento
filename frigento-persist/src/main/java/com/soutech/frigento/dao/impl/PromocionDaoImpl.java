package com.soutech.frigento.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.dao.PromocionDao;
import com.soutech.frigento.model.Promocion;

@Repository
@Transactional(readOnly=true)
public class PromocionDaoImpl extends AbstractSpringDao<Promocion, Integer> implements PromocionDao {

	public List<Promocion> obtenerPromociones(Boolean vigente, String sortFieldName, String sortOrder){
		StringBuilder hql = new StringBuilder("from ");
		hql.append(Promocion.class.getCanonicalName());
		hql.append(" p ");
		if(vigente != null && vigente){
			hql.append("where p.fechaHasta is null");
		}else if(vigente != null){
			hql.append("where p.fechaHasta is not null");
		}
		if(sortFieldName != null){
			hql.append(" order by p.");
			hql.append(sortFieldName);
			hql.append(" ");
			hql.append(sortOrder);
		}
		return findQuery(hql.toString());
		
	}
}
