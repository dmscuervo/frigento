package com.soutech.frigento.dao;
import java.util.Date;
import java.util.List;

import com.soutech.frigento.model.RelPedidoProducto;

public interface RelPedidoProductoDao extends IDao<RelPedidoProducto, Long> {

	Date findMinFechaPedido(Integer idProd);

	List<RelPedidoProducto> findAll(Short[] estadoPedidoId, String sortFieldName, String sortOrder);

	List<RelPedidoProducto> findAllByPedido(Integer idPedido);
}
