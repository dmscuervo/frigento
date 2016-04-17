package com.soutech.frigento.service;

import java.util.List;

import com.soutech.frigento.exception.ProductoSinCostoException;
import com.soutech.frigento.model.Pedido;

public interface PedidoService {

	boolean generarPedido(Pedido pedido) throws ProductoSinCostoException;

	List<Pedido> obtenerPedidos(Short[] estado, String sortFieldName, String sortOrder);

	Pedido obtenerPedido(Integer idPedido);

	boolean actualizarPedido(Pedido pedido) throws ProductoSinCostoException;

	void anularPedido(Integer pedidoId);

	void cumplirPedido(Integer pedidoId);

}
