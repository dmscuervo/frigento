package com.soutech.frigento.service;

import java.util.List;

import com.soutech.frigento.exception.EntityExistException;
import com.soutech.frigento.exception.StockAlteradoException;
import com.soutech.frigento.model.Producto;

public interface ProductoService {

	void saveProducto(Producto producto) throws EntityExistException;

	Producto obtenerProducto(Integer id);

	List<Producto> obtenerProductos(String estado, String sortFieldName, String sortOrder);

	void actualizarProducto(Producto producto) throws StockAlteradoException;

	void eliminarProducto(Producto producto);

	void reactivarProducto(Producto producto);

}
