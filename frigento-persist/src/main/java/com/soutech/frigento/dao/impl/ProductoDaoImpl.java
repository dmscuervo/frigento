package com.soutech.frigento.dao.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.dao.ProductoDao;
import com.soutech.frigento.exception.ControlStockRequeridoException;
import com.soutech.frigento.model.Producto;
import com.soutech.frigento.util.Constantes;

@Repository
@Transactional(readOnly=true)
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Producto> findAll(String estado, String sortFieldName, String sortOrder) {
		StringBuilder hql = new StringBuilder("from ");
		hql.append(Producto.class.getCanonicalName());
		hql.append(" p ");
		hql.append(" where p.fechaBaja is ");
		if(estado.equals(Constantes.ESTADO_ACTIVO)){
			hql.append("null ");
		}else if(estado.equals(Constantes.ESTADO_INACTIVO)){
			hql.append("not null ");
		}
		if(sortFieldName != null){
			hql.append("order by ");
			hql.append(sortFieldName);
			hql.append(" ");
			hql.append(sortOrder);
		}
		Query query = getSession().createQuery(hql.toString());
		return query.list();
	}

	public void update(Producto obj) {
		if(!obj.getStockControlado()){
			throw new ControlStockRequeridoException();
		}
		super.update(obj);
	}
	
	public void saveOrUpdate(Producto obj) {
		if(!obj.getStockControlado()){
			throw new ControlStockRequeridoException();
		}
		super.saveOrUpdate(obj);
	}

	@Override
	public Float obtenerStock(Integer idProd) {
		StringBuilder hql = new StringBuilder("select p.stock from ");
		hql.append(Producto.class.getCanonicalName());
		hql.append(" p where p.id = :idProd");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("idProd", idProd);
		return (Float) query.uniqueResult();
	}
	
	@Transactional
	public void delete(Producto producto) {
		producto.setFechaBaja(new Date());
		update(producto);
	}
	
	@Transactional
	public void deleteAll(Collection<Producto> productos) {
		for (Producto obj : productos)
			//La baja de productos es logica
			delete(obj);
	}
	
	@Transactional
	@Override
	public void reactivar(Producto producto) {
		producto.setFechaBaja(null);
		update(producto);
	}
}
