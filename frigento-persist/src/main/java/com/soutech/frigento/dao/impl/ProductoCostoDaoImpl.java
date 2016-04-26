package com.soutech.frigento.dao.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.dao.ProductoCostoDao;
import com.soutech.frigento.model.ProductoCosto;
import com.soutech.frigento.util.Constantes;

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
	public Date getMinFechaDesde(Integer idProd) {
		StringBuilder hql = new StringBuilder("select min(pc.fechaDesde) from ");
		hql.append(ProductoCosto.class.getCanonicalName());
		hql.append(" pc where pc.producto.id = :idProd ");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("idProd", idProd);
		return (Date) query.uniqueResult();
	}

	@Override
	public ProductoCosto findByProductoFecha(Integer idProd, Date fecha) {
		StringBuilder hql = new StringBuilder("from ");
		hql.append(ProductoCosto.class.getCanonicalName());
		hql.append(" pc where pc.producto.id = :idProd ");
		hql.append("and pc.fechaDesde <= :fecha ");
		hql.append("and (pc.fechaHasta is null or pc.fechaHasta > :fecha)");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("idProd", idProd);
		query.setParameter("fecha", fecha);
		return (ProductoCosto) query.uniqueResult();
	}

	@Override
	public Date getMinFechaHasta(Integer idProd) {
		StringBuilder hql = new StringBuilder("select min(pc.fechaHasta) from ");
		hql.append(ProductoCosto.class.getCanonicalName());
		hql.append(" pc where pc.producto.id = :idProd ");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("idProd", idProd);
		return (Date) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductoCosto> findAll(String estadoRel, Date fecha, String sortFieldName, String sortOrder) {
		StringBuilder hql = new StringBuilder("from ");
		hql.append(ProductoCosto.class.getCanonicalName());
		hql.append(" pc ");
		if(estadoRel != null && estadoRel.equals(Constantes.ESTADO_REL_VIGENTE)){
			hql.append(" where pc.fechaHasta is null ");
		}else if(estadoRel != null && estadoRel.equals(Constantes.ESTADO_REL_NO_VIGENTE)){
			hql.append(" where pc.fechaHasta is not null ");
		}
		if(fecha != null){
			hql.append(" where pc.fechaDesde <= :fecha ");
			hql.append(" and (pc.fechaHasta is null or pc.fechaHasta > :fecha) ");
		}
		if(sortFieldName != null){
			hql.append("order by pc.");
			hql.append(sortFieldName);
			hql.append(" ");
			hql.append(sortOrder);
		}
		Query query = getSession().createQuery(hql.toString());
		if(fecha != null){
			query.setParameter("fecha", fecha);
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductoCosto> findAllFechaDesdeEntre(Integer idProd, Date fechaIni, Date fechaFin, String sortFieldName, String sortOrder) {
		StringBuilder hql = new StringBuilder("from ");
		hql.append(ProductoCosto.class.getCanonicalName());
		hql.append(" pc ");
		hql.append("where pc.producto.id = :idProd ");
		hql.append("and pc.fechaDesde >= :fecha1 and pc.fechaDesde < :fecha2 ");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("idProd", idProd);
		query.setParameter("fecha1", fechaIni);
		query.setParameter("fecha2", fechaFin);
		if(sortFieldName != null){
			hql.append("order by pc.");
			hql.append(sortFieldName);
			hql.append(" ");
			hql.append(sortOrder);
		}
		return query.list();
	}

	@Override
	public ProductoCosto findFechaDesde(Integer idProd, Date fechaDesde) {
		StringBuilder hql = new StringBuilder("from ");
		hql.append(ProductoCosto.class.getCanonicalName());
		hql.append(" pc ");
		hql.append("where pc.producto.id = :idProd ");
		hql.append("and pc.fechaDesde = :fechaDesde ");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("idProd", idProd);
		query.setParameter("fechaDesde", fechaDesde);
		return (ProductoCosto) query.uniqueResult();
	}
	
	@Override
	public Date findMaxFechaDesdeAnterior(Integer idProd, Date fechaDesde) {
		StringBuilder hql = new StringBuilder("select max(pc.fechaDesde) from ");
		hql.append(ProductoCosto.class.getCanonicalName());
		hql.append(" pc ");
		hql.append("where pc.producto.id = :idProd ");
		hql.append("and pc.fechaDesde < :fechaDesde ");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("idProd", idProd);
		query.setParameter("fechaDesde", fechaDesde);
		return (Date) query.uniqueResult();
	}
}
