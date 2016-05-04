package com.soutech.frigento.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.Type;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.dao.RelVentaProductoDao;
import com.soutech.frigento.dto.ConsultaGananciaDTO;
import com.soutech.frigento.model.Producto;
import com.soutech.frigento.model.RelVentaProducto;
import com.soutech.frigento.util.Constantes;
import com.soutech.frigento.util.Utils;

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
	public List<RelVentaProducto> findAllNoAnulada(Integer prodId, Short catId, Date fechaIni, Date fechaFin) {
		StringBuilder hql = new StringBuilder("from ");
		hql.append(RelVentaProducto.class.getCanonicalName());
		hql.append(" rvp inner join rvp.venta v ");
		hql.append("inner join rvp.relProductoCategoria rpc ");
		hql.append("where v.fechaAnulado is null ");
		hql.append("and rpc.producto.id = :prodId ");
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
	public Date obtenerFechaPrimerVentaNoAnulada(Integer prodId, Short catId, Date fechaIni, Date fechaFin) {
		StringBuilder hql = new StringBuilder("select min(v.fecha) from ");
		hql.append(RelVentaProducto.class.getCanonicalName());
		hql.append(" rvp inner join rvp.venta v ");
		hql.append("inner join rvp.relProductoCategoria rpc ");
		hql.append("where v.fechaAnulado is null ");
		hql.append("and rpc.categoria.id = :catId ");
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

	@SuppressWarnings("unchecked")
	@Override
	public List<RelVentaProducto> findAllByVenta(Integer idVta, String sortFieldName, String sortOrder) {
		StringBuilder hql = new StringBuilder("from ");
		hql.append(RelVentaProducto.class.getCanonicalName());
		hql.append(" rvp ");
		hql.append("where rvp.venta.id = :idVta ");
		if(sortFieldName != null){
			hql.append("order by rvp.");
			hql.append(sortFieldName);
			hql.append(" ");
			hql.append(sortOrder);
		}
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("idVta", idVta);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaGananciaDTO> obtenerGanancia(Short tipo, String periodo, Short agrupamiento) {
		StringBuilder sql = new StringBuilder("");
		if(Constantes.CONSULTA_TIPO_RESUMEN.equals(tipo)){
			
			sql.append("select date_part('month', v.fecha) as mes, sum((rvp.precio-pc.costo)*rvp.cantidad) as ganancia ");
			
		}else if(Constantes.CONSULTA_TIPO_DETALLADO.equals(tipo)){
			if(agrupamiento.equals(Constantes.CONSULTA_GANANCIA_PRODUCTO)){
				
				sql.append("select producto.*, rvp.id_promocion as promo, sum(pc.costo) as costo, sum(rvp.cantidad) as cantidad, sum(rvp.precio) as importeVenta, sum((rvp.precio-pc.costo)*rvp.cantidad) as ganancia ");
				
			}else if(agrupamiento.equals(Constantes.CONSULTA_GANANCIA_VENTA)){
				
				
				
			}else if(agrupamiento.equals(Constantes.CONSULTA_GANANCIA_AMBOS)){
				
				
				
			}
		}
		
		sql.append("from rel_venta_producto rvp ");
		sql.append("inner join venta v on v.id_venta = rvp.id_venta ");
		sql.append("inner join rel_producto_categoria rpc on rvp.id_rel_prod_cat = rpc.id_rel_prod_cat ");
		sql.append("inner join producto_costo pc on pc.id_producto = rpc.id_producto ");
		sql.append("inner join producto producto on producto.id_producto = rpc.id_producto ");
		sql.append("and (pc.f_desde <= v.fecha and pc.f_hasta is null ");
				sql.append("or pc.f_desde <= v.fecha and pc.f_hasta > v.fecha) ");
		sql.append("where v.fecha between :fechaDesde and :fechaHasta ");
		
		if(Constantes.CONSULTA_TIPO_RESUMEN.equals(tipo)){
			
			sql.append("group by date_part('month', v.fecha)");
			sql.append("order by 1");
			
		}else if(Constantes.CONSULTA_TIPO_DETALLADO.equals(tipo)){
			if(agrupamiento.equals(Constantes.CONSULTA_GANANCIA_PRODUCTO)){
			
				sql.append("group by producto.id_producto, producto.codigo, producto.descripcion, rvp.id_promocion ");
				sql.append("order by producto.codigo, producto.descripcion, rvp.id_promocion");
				
			}else if(agrupamiento.equals(Constantes.CONSULTA_GANANCIA_VENTA)){
				
				
				
			}else if(agrupamiento.equals(Constantes.CONSULTA_GANANCIA_AMBOS)){
				
				
				
			}
		}
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		Date fechas[] = Utils.getFechaDesdeYHasta(periodo);
		query.setParameter("fechaDesde", fechas[0]);
		query.setParameter("fechaHasta", fechas[1]);
		if(Constantes.CONSULTA_TIPO_RESUMEN.equals(tipo)){
			
			query.addScalar("mes", new IntegerType());
			query.addScalar("ganancia", new BigDecimalType());
			
		}else if(Constantes.CONSULTA_TIPO_DETALLADO.equals(tipo)){
			if(agrupamiento.equals(Constantes.CONSULTA_GANANCIA_PRODUCTO)){
			
				query.addEntity("producto", Producto.class);
				query.addScalar("promo", new IntegerType());
				query.addScalar("costo", new BigDecimalType());
				query.addScalar("cantidad", new FloatType());
				query.addScalar("importeVenta", new BigDecimalType());
				query.addScalar("ganancia", new BigDecimalType());
				
			}else if(agrupamiento.equals(Constantes.CONSULTA_GANANCIA_VENTA)){
				
				
				
			}else if(agrupamiento.equals(Constantes.CONSULTA_GANANCIA_AMBOS)){
				
				
				
			}
		}
		query.setResultTransformer(new AliasToBeanResultTransformer(ConsultaGananciaDTO.class));
		return query.list();
	}
	
	
}
