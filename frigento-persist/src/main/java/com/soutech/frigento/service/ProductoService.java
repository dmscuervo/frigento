package com.soutech.frigento.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.soutech.frigento.exception.EntityExistException;
import com.soutech.frigento.exception.FechaDesdeException;
import com.soutech.frigento.exception.StockAlteradoException;
import com.soutech.frigento.model.Producto;
import com.soutech.frigento.model.RelProductoCategoria;

public interface ProductoService {

	void saveProducto(Producto producto) throws EntityExistException;

	Producto obtenerProducto(Integer id);

	List<Producto> obtenerProductos(String estado, String sortFieldName, String sortOrder);

	void actualizarProducto(Producto producto) throws StockAlteradoException, FechaDesdeException;

	void eliminarProducto(Integer productoId);

	void reactivarProducto(Integer productoId);

	boolean asignarNuevoPrecio(List<RelProductoCategoria> relProdCats, Date fechaDesde, BigDecimal costo,
			BigDecimal[] incrementos) throws FechaDesdeException;

}
