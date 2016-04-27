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
import com.soutech.frigento.dto.ItemPedidoDTO;
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
    private PedidoDao pedidoDao;
	@Autowired
	private ProductoCostoDao ProductoCostoDao;
	@Autowired
	private RelPedidoProductoDao relPedidoProductoDao;
	@Autowired
	private ControlStockProducto controlStockProducto;
	
	@Override
	@Transactional
	public boolean generarPedido(Pedido pedido) throws ProductoSinCostoException {
		boolean hayPedido = Boolean.FALSE;
		try{
			ControlPedidoVsCostoProducto.aplicarFlags(Boolean.TRUE, "redirect:/".concat("pedido?estado=A&sortFieldName=id&sortOrder=desc"), "prodCosto.concurrencia.costo.error");
			BigDecimal costoTotal = BigDecimal.ZERO;
			List<RelPedidoProducto> relaciones = new ArrayList<RelPedidoProducto>();
			for (ItemPedidoDTO item : pedido.getItems()) {
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
	public boolean actualizarPedido(Pedido pedidoModificado) throws ProductoSinCostoException {
		boolean hayPedido = Boolean.FALSE;
		try{
			ControlPedidoVsCostoProducto.aplicarFlags(Boolean.TRUE, "redirect:/".concat("pedido?estado=A&sortFieldName=descripcion&sortOrder=desc"), "prodCosto.concurrencia.costo.error");
			List<RelPedidoProducto> relacionesNuevas = new ArrayList<RelPedidoProducto>();
			List<RelPedidoProducto> relacionesModificadas = new ArrayList<RelPedidoProducto>();
			List<RelPedidoProducto> relacionesActuales = relPedidoProductoDao.findAllByPedido(pedidoModificado.getId(), null, null);
			List<RelPedidoProducto> relacionesEliminadas = new ArrayList<RelPedidoProducto>();
			Pedido pedidoActual = relacionesActuales.get(0).getPedido();
			
			BigDecimal costoTotal = controlStockProducto.verificarCambiosPedido(pedidoModificado, relacionesActuales, relacionesNuevas, relacionesModificadas, relacionesEliminadas);
			
			if(costoTotal == null){
				return hayPedido;
			}
			hayPedido = Boolean.TRUE;
			//Ahora persisto los datos. No lo podia hacer antes porque necesitaba saber si habia pedido (hayPedido)
			
			//Actualizo datos del pedido
			pedidoActual.setFecha(pedidoModificado.getFecha());
			pedidoActual.setEstado(pedidoModificado.getEstado());
			pedidoActual.setFechaAEntregar(pedidoModificado.getFechaAEntregar());
			pedidoActual.setCosto(costoTotal);
			if(!relacionesEliminadas.isEmpty() || !relacionesModificadas.isEmpty() || !relacionesNuevas.isEmpty()){
				if(pedidoModificado.getEstado().getId() > new Short(Constantes.ESTADO_PEDIDO_PENDIENTE)){
					pedidoActual.setVersion((short)(pedidoActual.getVersion()+1));
				}
			}
			pedidoDao.update(pedidoActual);
			
			//Elimino los productos dados de baja
			for (RelPedidoProducto rpp : relacionesEliminadas) {
				relPedidoProductoDao.delete(rpp);
			}
			//Actualizo las modificadas
			for (RelPedidoProducto rpp : relacionesModificadas) {
				relPedidoProductoDao.update(rpp);
			}
			//Producto nuevo
			for (RelPedidoProducto rpp : relacionesNuevas) {
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
	public void anularPedido(Integer pedidoId, Date fechaAnulado) {
		Pedido pedido = pedidoDao.findById(pedidoId);
		Estado estado = new Estado();
    	estado.setId(new Short(Constantes.ESTADO_PEDIDO_ANULADO));
		pedido.setEstado(estado);
		
		pedido.setFechaAnulado(new Date());
		if(fechaAnulado != null){
			pedido.setFechaEntregado(fechaAnulado);
		}
    	pedidoDao.update(pedido);
	}

	@Override
	@Transactional
	public void cumplirPedido(Pedido pedidoCumplido) throws ProductoSinCostoException {
		//Incrementar el stock
		Pedido pedido = controlStockProducto.incrementarStockBy(pedidoCumplido);
		//Cumplo el pedido
		pedido.setEstado(pedidoCumplido.getEstado());
		
		pedido.setFechaEntregado(new Date());
		pedidoDao.update(pedido);
	}

}