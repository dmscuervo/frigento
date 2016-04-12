package com.soutech.frigento.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.dao.PedidoDao;
import com.soutech.frigento.model.Pedido;

@Repository
@Transactional(readOnly=true)
public class PedidoDaoImpl extends AbstractSpringDao<Pedido, Integer> implements PedidoDao {

}
