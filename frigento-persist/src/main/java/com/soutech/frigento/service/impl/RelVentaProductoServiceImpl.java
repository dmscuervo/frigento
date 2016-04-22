package com.soutech.frigento.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soutech.frigento.dao.RelVentaProductoDao;
import com.soutech.frigento.model.RelVentaProducto;
import com.soutech.frigento.service.RelVentaProductoService;

@Service
public class RelVentaProductoServiceImpl implements RelVentaProductoService {

	@Autowired
    RelVentaProductoDao relVentaProductoDao;

	@Override
	public List<RelVentaProducto> obtenerVentas(Integer prodId, Short catId, Date fechaIni, Date fechaFin) {
		return relVentaProductoDao.findAll(prodId, catId, fechaIni, fechaFin);
	}

	@Override
	public Date obtenerFechaPrimerVenta(Integer prodId, Short catId, Date fechaIni, Date fechaFin) {
		return relVentaProductoDao.obtenerFechaPrimerVenta(prodId, catId, fechaIni, fechaFin);
	}

}
