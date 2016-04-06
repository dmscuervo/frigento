package com.soutech.frigento.service;

import com.soutech.frigento.model.Producto;

public interface ProductoService {

	void saveProducto(Producto producto);

	Object obtenerProducto(Integer id);

	Object obtenerProductos(String sortFieldName, String sortOrder);

	void actualizarProducto(Producto producto);

	void eliminarProducto(Producto producto);

}
