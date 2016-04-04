package com.soutech.frigento.dao.impl;

import org.springframework.stereotype.Repository;

import com.soutech.frigento.dao.PedidoDao;
import com.soutech.frigento.model.Pedido;

@Repository
public class PedidoDaoImpl extends AbstractSpringDao<Pedido, Integer> implements PedidoDao {

}
