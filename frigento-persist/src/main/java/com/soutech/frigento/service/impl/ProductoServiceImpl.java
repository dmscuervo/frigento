package com.soutech.frigento.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.dao.ProductoCostoDao;
import com.soutech.frigento.dao.ProductoDao;
import com.soutech.frigento.dao.RelProductoCategoriaDao;
import com.soutech.frigento.exception.EntityExistException;
import com.soutech.frigento.exception.StockAlteradoException;
import com.soutech.frigento.model.Producto;
import com.soutech.frigento.model.ProductoCosto;
import com.soutech.frigento.model.RelProductoCategoria;
import com.soutech.frigento.service.ProductoService;

@Service
public class ProductoServiceImpl implements ProductoService {	

	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
    ProductoDao productoDao;
	@Autowired
    ProductoCostoDao productoCostoDao;
	@Autowired
    RelProductoCategoriaDao relProductoCategoriaDao;
	@Autowired
    ControlStockProducto controlStockProducto;

	@Override
	@Transactional
	public void saveProducto(Producto producto) throws EntityExistException {
		Producto prod = productoDao.findByCodigo(producto.getCodigo());
		if(prod != null){
			throw new EntityExistException("codigo");
		}
		productoDao.save(producto);
		ProductoCosto rpc = new ProductoCosto();
		rpc.setCosto(producto.getCostoActual());
		rpc.setFechaDesde(producto.getFechaAlta());
		rpc.setProducto(producto);
		productoCostoDao.save(rpc);
	}

	@Override
	public Producto obtenerProducto(Integer id) {
		return productoDao.findById(id);
	}

	@Override
	public List<Producto> obtenerProductos(String estado, String sortFieldName, String sortOrder) {
		return productoDao.findAll(estado, sortFieldName, sortOrder);
	}

	@Override
	@Transactional
	public void actualizarProducto(Producto producto) throws StockAlteradoException {
		//Primero actualizo productoCosto
		ProductoCosto prodCosto = productoCostoDao.findByProductoFecha(producto.getId(), producto.getFechaAlta());
		prodCosto.setCosto(producto.getCostoActual());
		prodCosto.setFechaDesde(producto.getFechaAlta());
		productoCostoDao.update(prodCosto);
		
		controlStockProducto.actualizarProductoStock(producto);
	}

	@Transactional
	@Override
	public void eliminarProducto(Producto producto) {
		controlStockProducto.eliminarProductoStock(producto.getId());
		ProductoCosto prodCosto = productoCostoDao.findCostoActual(producto.getId());
		if(prodCosto != null){
			logger.info("El producto contiene relaciones con ProductoCosto. Se procede a su baja logica.");
			productoCostoDao.delete(prodCosto);
		}
		RelProductoCategoria relProdCategoria = relProductoCategoriaDao.findRelacionActual(producto.getId());
		if(relProdCategoria != null){
			logger.info("El producto contiene relaciones con RelProductoCategoria. Se procede a su baja logica.");
			relProductoCategoriaDao.delete(relProdCategoria);
		}
	}

	@Override
	@Transactional
	public void reactivarProducto(Producto producto) {
		ProductoCosto rpc = new ProductoCosto();
		rpc.setCosto(producto.getCostoActual());
		rpc.setFechaDesde(new Date());
		rpc.setProducto(producto);
		productoCostoDao.save(rpc);
		controlStockProducto.reactivarProductoStock(producto.getId());
	}

}