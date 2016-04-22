package com.soutech.frigento.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.dao.RelVentaProductoDao;
import com.soutech.frigento.model.RelVentaProducto;

@Repository
@Transactional(readOnly=true)
public class RelVentaProductoDaoImpl extends AbstractSpringDao<RelVentaProducto, Long> implements RelVentaProductoDao {

	@Override
	public Date findMaxFechaNoAnulada(Integer idProd) {
		StringBuilder hql = new StringBuilder("select max(v.fecha) from ");
		hql.append(RelVentaProducto.class.getCanonicalName());
		hql.append(" rvp inner join rvp.venta v ");
		hql.append("inner join rvp.relProductoCategoria rpc ");
		hql.append("inner join rpc.producto p ");
		hql.append("where v.fechaAnulado is null ");
		hql.append("and p.id = :prodId ");
		hql.append("and v.fecha >= rpc.fechaDesde ");
		hql.append("and (rpc.fechaHasta is null or v.fecha < rpc.fechaHasta) ");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("prodId", idProd);
		return (Date) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RelVentaProducto> findAll(Integer prodId, Short catId, Date fechaIni, Date fechaFin) {
		StringBuilder hql = new StringBuilder("from ");
		hql.append(RelVentaProducto.class.getCanonicalName());
		hql.append(" rvp inner join rvp.venta v ");
		hql.append("inner join rvp.relProductoCategoria rpc ");
		hql.append("where rpc.producto.id = :prodId ");
		hql.append("and rpc.categoria.id = :catId ");
		if(fechaIni != null){
			hql.append("and v.fecha >= :fechaIni ");
		}
		if(fechaFin != null){
			hql.append("and v.fecha < :fechaFin ");
		}
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("prodId", prodId);
		query.setParameter("catId", catId);
		if(fechaIni != null){
			query.setParameter("fechaIni", fechaIni);
		}
		if(fechaFin != null){
			query.setParameter("fechaFin", fechaFin);
		}
		return query.list();
	}

	@Override
	public Date obtenerFechaPrimerVenta(Integer prodId, Short catId, Date fechaIni, Date fechaFin) {
		StringBuilder hql = new StringBuilder("select min(v.fecha) from ");
		hql.append(RelVentaProducto.class.getCanonicalName());
		hql.append(" rvp inner join rvp.venta v ");
		hql.append("inner join rvp.relProductoCategoria rpc ");
		hql.append("where rpc.categoria.id = :catId ");
		hql.append("and rpc.producto.id = :prodId ");
		if(fechaIni != null){
			hql.append("and v.fecha >= :fechaIni ");
		}
		if(fechaFin != null){
			hql.append("and v.fecha < :fechaFin ");
		}
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("prodId", prodId);
		query.setParameter("catId", catId);
		if(fechaIni != null){
			query.setParameter("fechaIni", fechaIni);
		}
		if(fechaFin != null){
			query.setParameter("fechaFin", fechaFin);
		}
		return (Date) query.uniqueResult();
	}
	
}
