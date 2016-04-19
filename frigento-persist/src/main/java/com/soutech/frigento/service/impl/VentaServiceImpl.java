package com.soutech.frigento.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.concurrence.ControlVentaVsPrecioProducto;
import com.soutech.frigento.dao.ProductoCostoDao;
import com.soutech.frigento.dao.RelVentaProductoDao;
import com.soutech.frigento.dao.VentaDao;
import com.soutech.frigento.dto.ItemVentaDTO;
import com.soutech.frigento.exception.ProductoSinCostoException;
import com.soutech.frigento.model.RelProductoCategoria;
import com.soutech.frigento.model.RelVentaProducto;
import com.soutech.frigento.model.Venta;
import com.soutech.frigento.service.VentaService;

@Service
public class VentaServiceImpl implements VentaService {

	@Autowired
    VentaDao ventaDao;
	@Autowired
	ProductoCostoDao productoCostoDao;
	@Autowired
	RelVentaProductoDao relVentaProductoDao;

	@Override
	@Transactional
	public boolean generarVenta(Venta venta) throws ProductoSinCostoException {
		boolean hayPedido = Boolean.FALSE;
		try{
			ControlVentaVsPrecioProducto.aplicarFlags(Boolean.TRUE, "redirect:/".concat("venta?estado=A&sortFieldName=id&sortOrder=desc"), "relProdCat.concurrencia.precio.error");
			BigDecimal importeTotal = BigDecimal.ZERO;
			List<RelVentaProducto> relaciones = new ArrayList<RelVentaProducto>();
			for (ItemVentaDTO item : venta.getItems()) {
				if(item.getCantidad() != (short)0){
					hayPedido = Boolean.TRUE;
					RelVentaProducto rpp = new RelVentaProducto();
					rpp.setVenta(venta);
					RelProductoCategoria rpc = new RelProductoCategoria();
					rpc.setId(item.getRelProductoCategoriaId());
					rpp.setRelProductoCategoria(rpc );
					rpp.setCantidad(item.getCantidad());
					relaciones.add(rpp);
					//Voy calculando el costo total del pedido
					importeTotal = importeTotal.add(new BigDecimal(item.getCantidad()).multiply(item.getImporteVenta())).setScale(2, RoundingMode.HALF_UP);
				}
			}
			if(!hayPedido){
				return hayPedido;
			}
			
			venta.setImporte(importeTotal);
			venta.setVersion(new Short("1"));
			ventaDao.save(venta);
			//Guardo las relaciones
			for (RelVentaProducto rvp : relaciones) {
				rvp.setVenta(venta);
				relVentaProductoDao.save(rvp);
			}
			
		}finally{
			//Quito control
			ControlVentaVsPrecioProducto.aplicarFlags(Boolean.FALSE);
		}
		return hayPedido;
	}

	@Override
	public List<Venta> obtenerVentas(Short[] estado, String sortFieldName, String sortOrder) {
		return ventaDao.findAll(estado, sortFieldName, sortOrder);
	}

	@Override
	public Venta obtenerVenta(Integer idVenta) {
		return ventaDao.findById(idVenta);
	}

	@Override
	public boolean actualizarVenta(Venta venta) throws ProductoSinCostoException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void anularVenta(Integer ventaId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cumplirVenta(Venta ventaCumplido) throws ProductoSinCostoException {
		// TODO Auto-generated method stub
		
	}

}
