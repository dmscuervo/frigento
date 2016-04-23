package com.soutech.frigento.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.concurrence.ControlVentaVsPrecioProducto;
import com.soutech.frigento.dao.ProductoCostoDao;
import com.soutech.frigento.dao.RelVentaProductoDao;
import com.soutech.frigento.dao.VentaDao;
import com.soutech.frigento.dto.ItemVentaDTO;
import com.soutech.frigento.exception.ProductoSinCategoriaException;
import com.soutech.frigento.model.Estado;
import com.soutech.frigento.model.RelProductoCategoria;
import com.soutech.frigento.model.RelVentaProducto;
import com.soutech.frigento.model.Venta;
import com.soutech.frigento.service.VentaService;
import com.soutech.frigento.util.Constantes;

@Service
public class VentaServiceImpl implements VentaService {

	@Autowired
    VentaDao ventaDao;
	@Autowired
	ProductoCostoDao productoCostoDao;
	@Autowired
	RelVentaProductoDao relVentaProductoDao;
	@Autowired
	ControlStockProducto controlStockProducto;

	@Override
	@Transactional
	public boolean generarVenta(Venta venta) throws ProductoSinCategoriaException {
		boolean hayPedido = Boolean.FALSE;
		try{
			ControlVentaVsPrecioProducto.aplicarFlags(Boolean.TRUE, "redirect:/".concat("venta?estado=A&sortFieldName=id&sortOrder=desc"), "relProdCat.concurrencia.precio.error");
			BigDecimal importeTotal = BigDecimal.ZERO;
			List<RelVentaProducto> relaciones = new ArrayList<RelVentaProducto>();
			for (ItemVentaDTO item : venta.getItems()) {
				if(item.getCantidad() != (short)0){
					item.setCantidadModificada(item.getCantidad());
					hayPedido = Boolean.TRUE;
					RelVentaProducto rpp = new RelVentaProducto();
					rpp.setVenta(venta);
					RelProductoCategoria rpc = new RelProductoCategoria();
					rpc.setId(item.getRelProductoCategoriaId());
					rpp.setRelProductoCategoria(rpc);
					rpp.setCantidad(item.getCantidad());
					rpp.setPrecioVenta(item.getImporteVenta());
					relaciones.add(rpp);
					//Voy calculando el costo total del pedido
					importeTotal = importeTotal.add(new BigDecimal(item.getCantidad()).multiply(item.getImporteVenta()).setScale(2, RoundingMode.HALF_UP));
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
			
			//Decrementar el stock
			if(venta.getEstado().getId().equals(new Short(Constantes.ESTADO_PEDIDO_CONFIRMADO))){
				controlStockProducto.decrementarStockBy(venta);
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
	@Transactional
	public boolean actualizarVenta(Venta ventaModificada) throws ProductoSinCategoriaException {
		boolean hayPedido = Boolean.FALSE;
		try{
			ControlVentaVsPrecioProducto.aplicarFlags(Boolean.TRUE, "redirect:/".concat("venta?estado=A&sortFieldName=id&sortOrder=desc"), "relProdCat.concurrencia.precio.error");
			List<RelVentaProducto> relacionesNuevas = new ArrayList<RelVentaProducto>();
			List<RelVentaProducto> relacionesModificadas = new ArrayList<RelVentaProducto>();
			List<RelVentaProducto> relacionesActuales = relVentaProductoDao.findAllByVenta(ventaModificada.getId(), null, null);
			List<RelVentaProducto> relacionesEliminadas = new ArrayList<RelVentaProducto>();
			Venta ventaActual = relacionesActuales.get(0).getVenta();
			
			BigDecimal importeTotal = controlStockProducto.verificarCambiosVenta(ventaModificada, relacionesActuales, relacionesNuevas, relacionesModificadas, relacionesEliminadas);
			
			if(importeTotal == null){
				return hayPedido;
			}
			hayPedido = Boolean.TRUE;
			//Ahora persisto los datos. No lo podia hacer antes porque necesitaba saber si habia pedido (hayPedido)
			
			//Actualizo datos del pedido
			ventaActual.setFecha(ventaModificada.getFecha());
			ventaActual.setEstado(ventaModificada.getEstado());
			ventaActual.setFechaAEntregar(ventaModificada.getFechaAEntregar());
			ventaActual.setImporte(importeTotal);
			if(!relacionesEliminadas.isEmpty() || !relacionesModificadas.isEmpty() || !relacionesNuevas.isEmpty()){
				if(ventaModificada.getEstado().getId() > new Short(Constantes.ESTADO_PEDIDO_PENDIENTE)){
					ventaActual.setVersion((short)(ventaActual.getVersion()+1));
				}
			}
			ventaDao.update(ventaActual);
			
			//Elimino los productos dados de baja
			for (RelVentaProducto rvp : relacionesEliminadas) {
				relVentaProductoDao.delete(rvp);
			}
			//Actualizo las modificadas
			for (RelVentaProducto rvp : relacionesModificadas) {
				relVentaProductoDao.update(rvp);
			}
			//Producto nuevo
			for (RelVentaProducto rvp : relacionesNuevas) {
				relVentaProductoDao.save(rvp);
			}
			
			//Decrementar el stock
			if(ventaActual.getEstado().getId().equals(new Short(Constantes.ESTADO_PEDIDO_CONFIRMADO))){
				controlStockProducto.decrementarStockBy(ventaModificada);
			}
			
		}finally{
			//Quito control
			ControlVentaVsPrecioProducto.aplicarFlags(Boolean.FALSE);
		}
		return hayPedido;
	}

	@Transactional
	@Override
	public void anularVenta(Integer ventaId) {
		List<RelVentaProducto> relVtaProdList = relVentaProductoDao.findAllByVenta(ventaId, null, null);
		Venta venta = relVtaProdList.get(0).getVenta();
		
		if(venta.getEstado().getId().equals(new Short(Constantes.ESTADO_PEDIDO_CONFIRMADO))){
			//Tengo que incrementar el stock
			controlStockProducto.incrementarStockBy(relVtaProdList);
		}
		
		Estado estado = new Estado();
    	estado.setId(new Short(Constantes.ESTADO_PEDIDO_ANULADO));
		venta.setEstado(estado);
		
		venta.setFechaAnulado(new Date());
		ventaDao.update(venta);
	}

	@Override
	public void cumplirVenta(Integer ventaId) {
		Venta venta = ventaDao.findById(ventaId);
		Estado estado = new Estado();
    	estado.setId(new Short(Constantes.ESTADO_PEDIDO_ENTREGADO));
		venta.setEstado(estado);
		
		venta.setFechaEntregado(new Date());
		ventaDao.update(venta);
	}

}
