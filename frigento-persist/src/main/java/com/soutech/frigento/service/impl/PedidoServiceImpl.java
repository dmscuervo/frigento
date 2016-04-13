package com.soutech.frigento.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soutech.frigento.concurrence.ControlPedidoVsCostoProducto;
import com.soutech.frigento.dao.PedidoDao;
import com.soutech.frigento.dao.RelPedidoProductoDao;
import com.soutech.frigento.model.Pedido;
import com.soutech.frigento.model.RelPedidoProducto;
import com.soutech.frigento.service.PedidoService;

@Service
public class PedidoServiceImpl implements PedidoService {

	@Autowired
    PedidoDao pedidoDao;
	@Autowired
    RelPedidoProductoDao relPedidoProductoDao;
	
	@Override
	public boolean generarPedido(Pedido pedido) {
		boolean hayPedido = Boolean.FALSE;
		try{
			ControlPedidoVsCostoProducto.aplicarFlags(Boolean.TRUE, "redirect:/".concat("pedido?estado=A&sortFieldName=descripcion&sortOrder=asc"), "prodCosto.concurrencia.costo.error");
			BigDecimal costoTotal = BigDecimal.ZERO;
			for (RelPedidoProducto rpp : pedido.getItemsView()) {
				if(rpp.getCantidad() != (short)0){
					hayPedido = Boolean.TRUE;
					//Establezco costo del producto a nivel detalle del pedido
					rpp.setCosto(rpp.getProductoCosto().getCosto());
					//Voy calculando el costo total del pedido
					costoTotal = costoTotal.add(rpp.getProductoCosto().getCosto().multiply(new BigDecimal(rpp.getCantidad())).setScale(2, RoundingMode.HALF_UP));
				}
			}
			if(!hayPedido){
				return hayPedido;
			}
			
			pedido.setCosto(costoTotal);
			pedidoDao.save(pedido);
			//Guardo las relaciones
			for (RelPedidoProducto rpp : pedido.getItemsView()) {
				rpp.setPedido(pedido);
				relPedidoProductoDao.save(rpp);
			}
			
		}finally{
			//Quito control
			ControlPedidoVsCostoProducto.aplicarFlags(Boolean.FALSE);
		}
		return hayPedido;
	}

}