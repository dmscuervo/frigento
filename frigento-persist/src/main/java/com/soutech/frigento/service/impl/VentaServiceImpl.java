package com.soutech.frigento.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.dao.VentaDao;
import com.soutech.frigento.service.VentaService;

@Service
@Transactional
public class VentaServiceImpl implements VentaService {

	@Autowired
    VentaDao ventaDao;

}
