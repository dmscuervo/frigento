package com.soutech.frigento.dao;
import java.util.Date;

import com.soutech.frigento.model.RelPedidoProducto;

public interface RelPedidoProductoDao extends IDao<RelPedidoProducto, Long> {

	Date findMinFechaPedido(Integer idProd);
}
