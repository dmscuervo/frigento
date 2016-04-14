package com.soutech.frigento.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soutech.frigento.dao.RelPedidoProductoDao;
import com.soutech.frigento.model.RelPedidoProducto;
import com.soutech.frigento.service.RelPedidoProductoService;

@Service
public class RelPedidoProductoServiceImpl implements RelPedidoProductoService {

	@Autowired
    RelPedidoProductoDao relPedidoProductoDao;

	@Override
	public Date obtenerMinFechaPedido(Integer idProd) {
		return relPedidoProductoDao.findMinFechaPedido(idProd);
	}

	@Override
	public List<RelPedidoProducto> obtenerPedidos(Short[] estadoPedidoId, String sortFieldName, String sortOrder) {
		return relPedidoProductoDao.findAll(estadoPedidoId, sortFieldName, sortOrder);
	}

	@Override
	public List<RelPedidoProducto> obtenerByPedido(Integer idPedido) {
		return relPedidoProductoDao.findAllByPedido(idPedido);
	}

}
