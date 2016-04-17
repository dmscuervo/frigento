package com.soutech.frigento.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.soutech.frigento.dao.ProductoDao;
import com.soutech.frigento.dao.RelPedidoProductoDao;
import com.soutech.frigento.exception.StockAlteradoException;
import com.soutech.frigento.model.Pedido;
import com.soutech.frigento.model.Producto;
import com.soutech.frigento.model.RelPedidoProducto;

@Component
public class ControlStockProducto {
	
	@Autowired
    ProductoDao productoDao;
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

	public synchronized Pedido incrementarStockBy(Integer pedidoId) {
		List<RelPedidoProducto> relPedProdList = relPedidoProductoDao.findAllByPedido(pedidoId, null, null);
		
		for (RelPedidoProducto rpp : relPedProdList) {
			Producto producto = rpp.getProductoCosto().getProducto();
			producto.setStock(producto.getStock()+rpp.getCantidad());
			
			producto.setStockControlado(Boolean.TRUE);
			productoDao.update(producto);
		}
		
		return relPedProdList.get(0).getPedido();
	}
}
