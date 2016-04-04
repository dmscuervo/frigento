package com.soutech.frigento.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.dao.RelPedidoProductoDao;
import com.soutech.frigento.service.RelPedidoProductoService;

@Service
@Transactional
public class RelPedidoProductoServiceImpl implements RelPedidoProductoService {

	@Autowired
    RelPedidoProductoDao relPedidoProductoDao;

}
