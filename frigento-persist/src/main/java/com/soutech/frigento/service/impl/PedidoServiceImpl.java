package com.soutech.frigento.service.impl;

import java.util.Date;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soutech.frigento.dao.PedidoDao;
import com.soutech.frigento.model.Pedido;
import com.soutech.frigento.service.PedidoService;

@Service
public class PedidoServiceImpl implements PedidoService {

	@Autowired
    PedidoDao pedidoDao;

	@Override
	public void generarPedido(Pedido pedidoForm) {
		// TODO Auto-generated method stub
		
	}

}