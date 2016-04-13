package com.soutech.frigento.service;

import java.util.Date;
import java.util.List;

import com.soutech.frigento.model.RelPedidoProducto;

public interface RelPedidoProductoService {

	Date obtenerMinFechaPedido(Integer idProd);

	List<RelPedidoProducto> obtenerPedidos(Short[] estadoPedidoId, String sortFieldName, String sortOrder);

}
