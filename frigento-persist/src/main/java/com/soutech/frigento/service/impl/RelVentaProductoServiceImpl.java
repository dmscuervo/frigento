package com.soutech.frigento.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.dao.RelVentaProductoDao;
import com.soutech.frigento.service.RelVentaProductoService;

@Service
@Transactional
public class RelVentaProductoServiceImpl implements RelVentaProductoService {

	@Autowired
    RelVentaProductoDao relVentaProductoDao;

}
