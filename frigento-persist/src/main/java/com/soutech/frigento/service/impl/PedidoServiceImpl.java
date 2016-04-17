package com.soutech.frigento.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.concurrence.ControlPedidoVsCostoProducto;
import com.soutech.frigento.dao.PedidoDao;
import com.soutech.frigento.dao.ProductoCostoDao;
import com.soutech.frigento.dao.RelPedidoProductoDao;
import com.soutech.frigento.dto.ItemDTO;
import com.soutech.frigento.exception.ProductoSinCostoException;
import com.soutech.frigento.model.Estado;
import com.soutech.frigento.model.Pedido;
import com.soutech.frigento.model.ProductoCosto;
import com.soutech.frigento.model.RelPedidoProducto;
import com.soutech.frigento.service.PedidoService;
import com.soutech.frigento.util.Constantes;

@Service
public class PedidoServiceImpl implements PedidoService {

	@Autowired
    PedidoDao pedidoDao;
	@Autowired
    ProductoCostoDao ProductoCostoDao;
	@Autowired
    RelPedidoProductoDao relPedidoProductoDao;
	
	@Override
	@Transactional
	public boolean generarPedido(Pedido pedido) throws ProductoSinCostoException {
		boolean hayPedido = Boolean.FALSE;
		try{
			ControlPedidoVsCostoProducto.aplicarFlags(Boolean.TRUE, "redirect:/".concat("pedido?estado=A&sortFieldName=descripcion&sortOrder=asc"), "prodCosto.concurrencia.costo.error");
			BigDecimal costoTotal = BigDecimal.ZERO;
			List<RelPedidoProducto> relaciones = new ArrayList<RelPedidoProducto>();
			for (ItemDTO item : pedido.getItems()) {
				if(item.getCantidad() != (short)0){
					hayPedido = Boolean.TRUE;
					RelPedidoProducto rpp = new RelPedidoProducto();
					ProductoCosto productoCosto = ProductoCostoDao.findByProductoFecha(item.getProducto().getId(), pedido.getFecha());
					if(productoCosto == null){
						Object[] args = new Object[]{item.getProducto().getCodigo(), pedido.getFecha()};
						throw new ProductoSinCostoException("pedido.confirmar.producto.sin.costo", args);
					}
					rpp.setProductoCosto(productoCosto);
					rpp.setCosto(productoCosto.getCosto());
					rpp.setCantidad(item.getCantidad() * productoCosto.getProducto().getPesoCaja());
					relaciones.add(rpp);
					//Voy calculando el costo total del pedido
					costoTotal = costoTotal.add(productoCosto.getCosto().multiply(new BigDecimal(rpp.getCantidad())).setScale(2, RoundingMode.HALF_UP));
				}
			}
			if(!hayPedido){
				return hayPedido;
			}
			
			pedido.setCosto(costoTotal);
			pedido.setVersion(new Short("1"));
			pedidoDao.save(pedido);
			//Guardo las relaciones
			for (RelPedidoProducto rpp : relaciones) {
				rpp.setPedido(pedido);
				relPedidoProductoDao.save(rpp);
			}
			
		}finally{
			//Quito control
			ControlPedidoVsCostoProducto.aplicarFlags(Boolean.FALSE);
		}
		return hayPedido;
	}
	
	@Override
	@Transactional
	public boolean actualizarPedido(Pedido pedido) throws ProductoSinCostoException {
		boolean hayPedido = Boolean.FALSE;
		try{
			Boolean prodNuevo;
			RelPedidoProducto rppActual;
			ControlPedidoVsCostoProducto.aplicarFlags(Boolean.TRUE, "redirect:/".concat("pedido?estado=A&sortFieldName=descripcion&sortOrder=asc"), "prodCosto.concurrencia.costo.error");
			BigDecimal costoTotal = BigDecimal.ZERO;
			List<RelPedidoProducto> relacionesNuevas = new ArrayList<RelPedidoProducto>();
			List<RelPedidoProducto> relacionesModificadas = new ArrayList<RelPedidoProducto>();
			List<RelPedidoProducto> relaciones = relPedidoProductoDao.findAllByPedido(pedido.getId(), null, null);
			List<RelPedidoProducto> prodEliminadosDelPed = new ArrayList<RelPedidoProducto>();
			Pedido pedidoActual = relaciones.get(0).getPedido();
			for (ItemDTO item : pedido.getItems()) {
				if(item.getCantidad() != (short)0){
					rppActual = null;
					prodNuevo = Boolean.TRUE;
					hayPedido = Boolean.TRUE;
					//Me fijo si es un producto nuevo o existente
					for (RelPedidoProducto rpp : relaciones) {
						if(rpp.getProductoCosto().getProducto().getId().equals(item.getProducto().getId())){
							prodNuevo = Boolean.FALSE;
							rppActual = rpp;
							break;
						}
					}
					ProductoCosto productoCosto = ProductoCostoDao.findByProductoFecha(item.getProducto().getId(), pedido.getFecha());
					if(prodNuevo){
						RelPedidoProducto rpp = new RelPedidoProducto();
						if(productoCosto == null){
							Object[] args = new Object[]{item.getProducto().getCodigo(), pedido.getFecha()};
							throw new ProductoSinCostoException("pedido.confirmar.producto.sin.costo", args);
						}
						rpp.setProductoCosto(productoCosto);
						rpp.setCosto(productoCosto.getCosto());
						rpp.setCantidad(item.getCantidad() * productoCosto.getProducto().getPesoCaja());
						relacionesNuevas.add(rpp);
						//Voy calculando el costo total del pedido
						costoTotal = costoTotal.add(productoCosto.getCosto().multiply(new BigDecimal(rpp.getCantidad())).setScale(2, RoundingMode.HALF_UP));
					}else{
						//Me fijo si cambio la cantidad
						Float cantidadNueva = item.getCantidad() * productoCosto.getProducto().getPesoCaja();
						if(rppActual.getCantidad() != cantidadNueva){
							rppActual.setCantidad(item.getCantidad() * productoCosto.getProducto().getPesoCaja());
							relacionesModificadas.add(rppActual);
						}
						//Voy calculando el costo total del pedido
						costoTotal = costoTotal.add(rppActual.getCosto().multiply(new BigDecimal(rppActual.getCantidad())).setScale(2, RoundingMode.HALF_UP));
					}
					
				}else{
					//Se saco un producto del pedido. Elimina la relacion
					for (RelPedidoProducto rpp : relaciones) {
						if(rpp.getProductoCosto().getProducto().getId().equals(item.getProducto().getId())){
							prodEliminadosDelPed.add(rpp);
							break;
						}
					}
				}
			}
			if(!hayPedido){
				return hayPedido;
			}
			//Ahora persisto los datos. No lo podia hacer antes porque necesitaba saber si habia pedido (hayPedido)
			
			//Actualizo datos del pedido
			pedidoActual.setFecha(pedido.getFecha());
			pedidoActual.setEstado(pedido.getEstado());
			pedidoActual.setFechaAEntregar(pedido.getFechaAEntregar());
			pedidoActual.setCosto(costoTotal);
			if(!prodEliminadosDelPed.isEmpty() || !relacionesModificadas.isEmpty() || !relacionesNuevas.isEmpty()){
				if(pedido.getEstado().getId() > new Short(Constantes.ESTADO_PEDIDO_PENDIENTE)){
					pedidoActual.setVersion((short)(pedidoActual.getVersion()+1));
				}
			}
			pedidoDao.update(pedidoActual);
			
			//Elimino los productos dados de baja
			for (RelPedidoProducto rpp : prodEliminadosDelPed) {
				relPedidoProductoDao.delete(rpp);
			}
			//Actualizo las modificadas
			for (RelPedidoProducto rpp : relacionesModificadas) {
				relPedidoProductoDao.update(rpp);
			}
			//Producto nuevo
			for (RelPedidoProducto rpp : relacionesNuevas) {
				rpp.setPedido(pedidoActual);
				relPedidoProductoDao.save(rpp);
			}
			
			
		}finally{
			//Quito control
			ControlPedidoVsCostoProducto.aplicarFlags(Boolean.FALSE);
		}
		return hayPedido;
	}

	@Override
	public List<Pedido> obtenerPedidos(Short[] estado, String sortFieldName, String sortOrder) {
		return pedidoDao.findAll(estado, sortFieldName, sortOrder);
	}

	@Override
	public Pedido obtenerPedido(Integer idPedido) {
		return pedidoDao.findById(idPedido);	
	}

	@Override
	public void anularPedido(Integer pedidoId) {
		Pedido pedido = pedidoDao.findById(pedidoId);
		Estado estado = new Estado();
    	estado.setId(new Short(Constantes.ESTADO_PEDIDO_ANULADO));
		pedido.setEstado(estado);
		
		pedido.setFechaAnulado(new Date());
    	pedidoDao.update(pedido);
	}

}