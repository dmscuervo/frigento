package com.soutech.frigento.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.dao.ProductoDao;
import com.soutech.frigento.exception.EntityExistException;
import com.soutech.frigento.model.Producto;
import com.soutech.frigento.service.ProductoService;

@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {	

	@Autowired
    ProductoDao productoDao;

	@Override
	public void saveProducto(Producto producto) throws EntityExistException {
		Producto prod = productoDao.findByCodigo(producto);
		if(prod != null){
			throw new EntityExistException("codigo");
		}
		productoDao.save(producto);
	}

	@Override
	public Producto obtenerProducto(Integer id) {
		return productoDao.findById(id);
	}

	@Override
	public List<Producto> obtenerProductos(String sortFieldName, String sortOrder) {
		return productoDao.findAll(sortFieldName, sortOrder);
	}

	@Override
	public void actualizarProducto(Producto producto) {
		productoDao.update(producto);
	}

	@Override
	public void eliminarProducto(Producto producto) {
		productoDao.delete(producto);
	}

	
}