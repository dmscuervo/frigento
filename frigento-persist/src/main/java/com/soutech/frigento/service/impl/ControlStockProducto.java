package com.soutech.frigento.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.soutech.frigento.dao.ProductoDao;
import com.soutech.frigento.exception.StockAlteradoException;
import com.soutech.frigento.model.Producto;

@Component
public class ControlStockProducto {
	
	@Autowired
    ProductoDao productoDao;

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
}
