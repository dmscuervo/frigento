package com.soutech.frigento.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.dao.RelPedidoProductoDao;
import com.soutech.frigento.model.RelPedidoProducto;
import com.soutech.frigento.util.Constantes;

@Repository
@Transactional(readOnly=true)
public class RelPedidoProductoDaoImpl extends AbstractSpringDao<RelPedidoProducto, Long> implements RelPedidoProductoDao {

	@Override
	public Date findMinFechaPedidoNoAnulado(Integer idProd) {
		StringBuilder hql = new StringBuilder("select min(rpp.pedido.fecha) from ");
		hql.append(RelPedidoProducto.class.getCanonicalName());
		hql.append(" rpp where rpp.productoCosto.producto.id = :idProd ");
		hql.append("and rpp.pedido.estado.id <> :idEstadoAnulado ");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("idProd", idProd);
		query.setParameter("idEstadoAnulado", new Short(Constantes.ESTADO_PEDIDO_ANULADO));
		return (Date) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RelPedidoProducto> findAll(Short[] estadoPedidoId, String sortFieldName, String sortOrder) {
		StringBuilder hql = new StringBuilder("from ");
		hql.append(RelPedidoProducto.class.getCanonicalName());
		hql.append(" rpp ");
		if(estadoPedidoId != null){
			hql.append("where rpp.pedido.estado.id in (:estadoId) ");
		}
		if(sortFieldName != null){
			hql.append("order by rpp.");
			hql.append(sortFieldName);
			hql.append(" ");
			hql.append(sortOrder);
		}
		Query query = getSession().createQuery(hql.toString());
		query.setParameterList("estadoId", estadoPedidoId);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RelPedidoProducto> findAllByPedido(Integer idPedido, String sortFieldName, String sortOrder) {
		StringBuilder hql = new StringBuilder("from ");
		hql.append(RelPedidoProducto.class.getCanonicalName());
		hql.append(" rpp ");
		hql.append("where rpp.pedido.id = :idPed ");
		if(sortFieldName != null){
			hql.append("order by rpp.");
			hql.append(sortFieldName);
			hql.append(" ");
			hql.append(sortOrder);
		}
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("idPed", idPedido);
		return query.list();
	}

}
