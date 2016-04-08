package com.soutech.frigento.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soutech.frigento.dao.RelPedidoProductoDao;
import com.soutech.frigento.service.RelPedidoProductoService;

@Service
public class RelPedidoProductoServiceImpl implements RelPedidoProductoService {

	@Autowired
    RelPedidoProductoDao relPedidoProductoDao;

}
