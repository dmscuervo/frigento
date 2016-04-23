package com.soutech.frigento.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.soutech.frigento.dao.ProductoCostoDao;
import com.soutech.frigento.dao.ProductoDao;
import com.soutech.frigento.dao.RelPedidoProductoDao;
import com.soutech.frigento.dao.RelProductoCategoriaDao;
import com.soutech.frigento.dto.ItemPedidoDTO;
import com.soutech.frigento.dto.ItemVentaDTO;
import com.soutech.frigento.exception.ProductoSinCategoriaException;
import com.soutech.frigento.exception.ProductoSinCostoException;
import com.soutech.frigento.exception.StockAlteradoException;
import com.soutech.frigento.model.Pedido;
import com.soutech.frigento.model.Producto;
import com.soutech.frigento.model.ProductoCosto;
import com.soutech.frigento.model.RelPedidoProducto;
import com.soutech.frigento.model.RelProductoCategoria;
import com.soutech.frigento.model.RelVentaProducto;
import com.soutech.frigento.model.Venta;

@Component
public class ControlStockProducto {
	
	@Autowired
    ProductoDao productoDao;
	@Autowired
    ProductoCostoDao productoCostoDao;
	@Autowired
    RelProductoCategoriaDao relProductoCategoriaDao;
	@Autowired
	private RelPedidoProductoDao relPedidoProductoDao;
	
	public synchronized void actualizarProductoStock(Producto producto) throws StockAlteradoException{
		if(producto.getStockPrevio() != null){
			Float stock = productoDao.obtenerStock(producto.getId());
			if(!stock.equals(producto.getStockPrevio())){
				producto.setStock(stock);
				throw new StockAlteradoException(producto);
			}
		}
		
		//Actualizo el producto
		producto.setStockControlado(Boolean.TRUE);
		productoDao.update(producto);
	}
	
	public synchronized void eliminarProductoStock(Integer prodId) {
		Producto producto = productoDao.findById(prodId);
		//Baja logica de producto
		producto.setStockControlado(Boolean.TRUE);
		productoDao.delete(producto);
	}
	
	public synchronized void reactivarProductoStock(Integer prodId) {
		Producto producto = productoDao.findById(prodId);
		//Baja logica de producto
		producto.setStockControlado(Boolean.TRUE);
		productoDao.reactivar(producto);
	}

	/**
	 * Incremento el stock de un pedido. El mismo se produce cuando el pedido es pasado a cumplido, por lo cual se asume que pedidoCumplido tiene un id ya generado
	 * Tambien aplico control de cambios sobre las relaciones del pedido, persistiendo las diferencias en base de datos.
	 * @param pedidoCumplido
	 * @return Una instancia de Pedido atachada a la session con el monto total del pedido recalculado
	 * @throws ProductoSinCostoException
	 */
	public synchronized Pedido incrementarStockBy(Pedido pedidoCumplido) throws ProductoSinCostoException {
		
		List<RelPedidoProducto> relacionesActual = relPedidoProductoDao.findAllByPedido(pedidoCumplido.getId(), null, null);
		List<RelPedidoProducto> relacionesNuevas = new ArrayList<RelPedidoProducto>();
		List<RelPedidoProducto> relacionesModificadas = new ArrayList<RelPedidoProducto>();
		List<RelPedidoProducto> relacionesEliminadas = new ArrayList<RelPedidoProducto>();
		
		BigDecimal costoTotal = verificarCambiosPedido(pedidoCumplido, relacionesActual, relacionesNuevas, relacionesModificadas, relacionesEliminadas);
		
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
		
		//Actualizo stock
		for (ItemPedidoDTO item : pedidoCumplido.getItems()) {
			Producto producto = productoDao.findById(item.getProducto().getId());
			producto.setStock(producto.getStock() + (producto.getPesoCaja() * item.getCantidad()));
			producto.setStockControlado(Boolean.TRUE);
			productoDao.update(producto);
			
		}
		
		relacionesActual.get(0).getPedido().setCosto(costoTotal);
		return relacionesActual.get(0).getPedido();
	}
	
	/**
	 * Decrementa el stock de una venta. El mismo se produce cuando la venta es pasada a confirmada.
	 * @param pedidoCumplido
	 * @throws ProductoSinCostoException
	 */
	public synchronized void decrementarStockBy(Venta ventaConfirmada) {
		
		//Actualizo stock
		for (ItemVentaDTO item : ventaConfirmada.getItems()) {
			if(item.getCantidadModificada() != null){
				Producto producto = productoDao.findById(item.getProducto().getId());
				producto.setStock(producto.getStock() - item.getCantidadModificada());
				producto.setStockControlado(Boolean.TRUE);
				productoDao.update(producto);
			}
		}
		
	}
	
	/**
	 * Incrementa el stock de una venta anulada. El mismo se produce cuando la venta es pasada de confirmada a anulada.
	 * @param pedidoCumplido
	 * @throws ProductoSinCostoException
	 */
	public synchronized void incrementarStockBy(List<RelVentaProducto> relVtaProdList) {
		
		//Actualizo stock
		for (RelVentaProducto relVtaProd : relVtaProdList) {
			Producto producto = productoDao.findById(relVtaProd.getRelProductoCategoria().getProducto().getId());
			producto.setStock(producto.getStock() + relVtaProd.getCantidad());
			producto.setStockControlado(Boolean.TRUE);
			productoDao.update(producto);
		}
		
	}
	
	/**
	 * Chequea cambios en los items de un pedido. Completa la informacion de los cambios en las relaciones (relacionesNuevas, relacionesModificadas y prodEliminadosDelPed).
	 * Ademas, calcula el costo total del pedido en (costoTotal)
	 * @param pedidoModificado
	 * @param relacionesActual
	 * @param relacionesNuevas
	 * @param relacionesModificadas
	 * @param relacionesEliminadas
	 * @param costoTotal
	 * @return
	 * @throws ProductoSinCostoException
	 */
	public BigDecimal verificarCambiosPedido(Pedido pedidoModificado, List<RelPedidoProducto> relacionesActual, List<RelPedidoProducto> relacionesNuevas,
		List<RelPedidoProducto> relacionesModificadas, List<RelPedidoProducto> relacionesEliminadas) throws ProductoSinCostoException {
		BigDecimal costoTotal = null;
		Boolean prodNuevo;
		RelPedidoProducto rppActual;
		for (ItemPedidoDTO item : pedidoModificado.getItems()) {
			if(item.getCantidad() != (short)0){
				rppActual = null;
				prodNuevo = Boolean.TRUE;
				//Inicializo costoTotal (solo la primera vez)
				costoTotal = costoTotal == null ? BigDecimal.ZERO : costoTotal;
				//Me fijo si es un producto nuevo o existente
				for (RelPedidoProducto rpp : relacionesActual) {
					if(rpp.getProductoCosto().getProducto().getId().equals(item.getProducto().getId())){
						prodNuevo = Boolean.FALSE;
						rppActual = rpp;
						break;
					}
				}
				ProductoCosto productoCosto = productoCostoDao.findByProductoFecha(item.getProducto().getId(), pedidoModificado.getFecha());
				if(prodNuevo){
					RelPedidoProducto rpp = new RelPedidoProducto();
					if(productoCosto == null){
						Object[] args = new Object[]{item.getProducto().getCodigo(), pedidoModificado.getFecha()};
						throw new ProductoSinCostoException("pedido.confirmar.producto.sin.costo", args);
					}
					rpp.setProductoCosto(productoCosto);
					rpp.setCosto(productoCosto.getCosto());
					//Caso de un pedido a cumplir. Se toma el costo definido en pantalla
					if(item.getCostoCumplir() != null){
						rpp.setCosto(item.getCostoCumplir());
					}
					rpp.setCantidad(item.getCantidad() * productoCosto.getProducto().getPesoCaja());
					rpp.setPedido(relacionesActual.get(0).getPedido());
					relacionesNuevas.add(rpp);
					//Voy calculando el costo total del pedido
					costoTotal = costoTotal.add(rpp.getCosto().multiply(new BigDecimal(rpp.getCantidad())).setScale(2, RoundingMode.HALF_UP));
				}else{
					//Me fijo si cambio la cantidad
					Float cantidadNueva = item.getCantidad() * productoCosto.getProducto().getPesoCaja();
					if(!rppActual.getCantidad().equals(cantidadNueva)){
						rppActual.setCantidad(item.getCantidad() * productoCosto.getProducto().getPesoCaja());
						relacionesModificadas.add(rppActual);
					}else if(item.getCostoCumplir() != null){
						relacionesModificadas.add(rppActual);
					}
					BigDecimal costo = rppActual.getCosto();
					//Caso de un pedido a cumplir. Se toma el costo definido en pantalla
					if(item.getCostoCumplir() != null){
						costo = item.getCostoCumplir();
						rppActual.setCosto(costo);
					}
					//Voy calculando el costo total del pedido
					costoTotal = costoTotal.add(costo.multiply(new BigDecimal(rppActual.getCantidad())).setScale(2, RoundingMode.HALF_UP));
				}
				
			}else{
				//Se saco un producto del pedido. Elimina la relacion
				for (RelPedidoProducto rpp : relacionesActual) {
					if(rpp.getProductoCosto().getProducto().getId().equals(item.getProducto().getId())){
						relacionesEliminadas.add(rpp);
						break;
					}
				}
			}
		}
		return costoTotal;
	}

	/**
	 * Chequea cambios en los items de una venta. Completa la informacion de los cambios en las relaciones (relacionesNuevas, relacionesModificadas y relacionesEliminadas).
	 * Ademas, calcula el precio total de la venta en (precioTotal)
	 * @param ventaModificada
	 * @param relacionesActual
	 * @param relacionesNuevas
	 * @param relacionesModificadas
	 * @param relacionesEliminadas
	 * @param costoTotal
	 * @return
	 * @throws ProductoSinCostoException
	 */
	public BigDecimal verificarCambiosVenta(Venta ventaModificada, List<RelVentaProducto> relacionesActual, List<RelVentaProducto> relacionesNuevas,
									List<RelVentaProducto> relacionesModificadas, List<RelVentaProducto> relacionesEliminadas) throws ProductoSinCategoriaException {
		BigDecimal precioTotal = null;
		Boolean prodNuevo;
		RelVentaProducto rvpActual;
		for (ItemVentaDTO item : ventaModificada.getItems()) {
			if(item.getCantidad() != (short)0){
				rvpActual = null;
				prodNuevo = Boolean.TRUE;
				//Inicializo precioTotal (solo la primera vez)
				precioTotal = precioTotal == null ? BigDecimal.ZERO : precioTotal;
				//Me fijo si es un producto nuevo o existente
				for (RelVentaProducto rvp : relacionesActual) {
					if(rvp.getRelProductoCategoria().getProducto().getId().equals(item.getProducto().getId())){
						prodNuevo = Boolean.FALSE;
						rvpActual = rvp;
						break;
					}
				}
				RelProductoCategoria relProductoCategoria = relProductoCategoriaDao.findByDupla(ventaModificada.getUsuario().getCategoriaProducto().getId(), item.getProducto().getId(), ventaModificada.getFecha());
				if(prodNuevo){
					item.setCantidadModificada(item.getCantidad());
					RelVentaProducto rvp = new RelVentaProducto();
					if(relProductoCategoria == null){
						Object[] args = new Object[]{ventaModificada.getFecha(), item.getProducto().getCodigo(), ventaModificada.getUsuario().getCategoriaProducto().getDescripcion()};
						throw new ProductoSinCategoriaException("venta.confirmar.sin.producto.categoria", args);
					}
					rvp.setVenta(relacionesActual.get(0).getVenta());
					rvp.setRelProductoCategoria(relProductoCategoria);
					rvp.setCantidad(item.getCantidad());
					rvp.setPrecioVenta(item.getImporteVenta());
					relacionesNuevas.add(rvp);
					//Voy calculando el importe total de la venta
					precioTotal = precioTotal.add(new BigDecimal(item.getCantidad()).multiply(item.getImporteVenta()).setScale(2, RoundingMode.HALF_UP));

				}else{
					//Me fijo si cambio la cantidad
					Float cantidadNueva = item.getCantidad();
					if(!rvpActual.getCantidad().equals(cantidadNueva)){
						item.setCantidadModificada(item.getCantidad()-rvpActual.getCantidad());
						rvpActual.setCantidad(item.getCantidad());
						relacionesModificadas.add(rvpActual);
					}
					//Voy calculando el importe total de la venta
					precioTotal = precioTotal.add(rvpActual.getPrecioVenta().multiply(new BigDecimal(item.getCantidad())).setScale(2, RoundingMode.HALF_UP));
				}
				
			}else{
				//Se saco un producto de la venta. Elimino la relacion
				for (RelVentaProducto rpc : relacionesActual) {
					if(rpc.getRelProductoCategoria().getProducto().getId().equals(item.getProducto().getId())){
						relacionesEliminadas.add(rpc);
						break;
					}
				}
			}
		}
		return precioTotal;
	}
}
