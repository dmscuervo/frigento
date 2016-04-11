package com.soutech.frigento.dao.impl;

import java.util.Date;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.soutech.frigento.dao.RelVentaProductoDao;
import com.soutech.frigento.model.Producto;
import com.soutech.frigento.model.RelProductoCategoria;
import com.soutech.frigento.model.RelVentaProducto;
import com.soutech.frigento.model.Venta;

@Repository
public class RelVentaProductoDaoImpl extends AbstractSpringDao<RelVentaProducto, Long> implements RelVentaProductoDao {

	@Override
	public Date findMaxFechaNoAnulada(Integer idProd) {
		StringBuilder hql = new StringBuilder("select max(v.fecha) from ");
		hql.append(RelVentaProducto.class.getCanonicalName());
		hql.append(" rvp inner join rvp.venta");
		hql.append(" v inner join rvp.relProductoCategoria");
		hql.append(" rpc inner join rpc.producto ");
		hql.append(" p where v.fechaAnulado is null ");
		hql.append("and p.id = :prodId ");
		hql.append("and v.fecha >= rpc.fechaDesde ");
		hql.append("and (rpc.fechaHasta is null or v.fecha < rpc.fechaHasta) ");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("prodId", idProd);
		return (Date) query.uniqueResult();
	}
}
