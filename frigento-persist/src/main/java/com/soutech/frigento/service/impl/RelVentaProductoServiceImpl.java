package com.soutech.frigento.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soutech.frigento.dao.RelVentaProductoDao;
import com.soutech.frigento.service.RelVentaProductoService;

@Service
public class RelVentaProductoServiceImpl implements RelVentaProductoService {

	@Autowired
    RelVentaProductoDao relVentaProductoDao;

}
