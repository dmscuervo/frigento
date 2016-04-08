package com.soutech.frigento.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soutech.frigento.dao.VentaDao;
import com.soutech.frigento.service.VentaService;

@Service
public class VentaServiceImpl implements VentaService {

	@Autowired
    VentaDao ventaDao;

}
