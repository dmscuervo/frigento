package com.soutech.frigento.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.dao.PedidoDao;
import com.soutech.frigento.service.PedidoService;

@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {

	@Autowired
    PedidoDao pedidoDao;


}