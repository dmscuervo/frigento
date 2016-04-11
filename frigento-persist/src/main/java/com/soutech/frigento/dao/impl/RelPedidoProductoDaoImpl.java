package com.soutech.frigento.dao.impl;

import java.util.Date;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.dao.RelPedidoProductoDao;
import com.soutech.frigento.model.RelPedidoProducto;

@Repository
@Transactional(readOnly=true)
public class RelPedidoProductoDaoImpl extends AbstractSpringDao<RelPedidoProducto, Long> implements RelPedidoProductoDao {

	@Override
	public Date findMinFechaPedido(Integer idProd) {
		StringBuilder hql = new StringBuilder("select min(rpp.pedido.fecha) from ");
		hql.append(RelPedidoProducto.class.getCanonicalName());
		hql.append(" rpp where rpp.productoCosto.producto.id = :idProd ");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("idProd", idProd);
		return (Date) query.uniqueResult();
	}

}
