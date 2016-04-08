package com.soutech.frigento.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soutech.frigento.dao.PedidoDao;
import com.soutech.frigento.service.PedidoService;

@Service
public class PedidoServiceImpl implements PedidoService {

	@Autowired
    PedidoDao pedidoDao;


}