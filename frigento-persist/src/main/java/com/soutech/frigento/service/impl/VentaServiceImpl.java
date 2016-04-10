package com.soutech.frigento.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soutech.frigento.concurrence.ControlVentaVsPrecioProducto;
import com.soutech.frigento.dao.VentaDao;
import com.soutech.frigento.model.Venta;
import com.soutech.frigento.service.VentaService;

@Service
public class VentaServiceImpl implements VentaService {

	@Autowired
    VentaDao ventaDao;

	@Override
	public void generarVenta(Venta venta) {
		//Aplico control de concurrencia entre ventas y cambio de precios
		ControlVentaVsPrecioProducto.aplicarFlags(Boolean.TRUE, null);
		try{
			//Generar venta
		}finally{
			//Quito control
			ControlVentaVsPrecioProducto.aplicarFlags(Boolean.FALSE);
		}
	}

}
