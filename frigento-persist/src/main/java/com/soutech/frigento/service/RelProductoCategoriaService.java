package com.soutech.frigento.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.soutech.frigento.exception.FechaDesdeException;
import com.soutech.frigento.exception.ProductoInexistenteException;
import com.soutech.frigento.model.Categoria;
import com.soutech.frigento.model.RelProductoCategoria;

public interface RelProductoCategoriaService {

	List<RelProductoCategoria> obtenerProductosCategoria(Short idCat, String estado, String[] sortFieldName, String[] sortOrder);
	
	/**
	 * Obtiene las relaciones producto-categoria segun una fecha de venta y para una categoria correspondiente a un usuario.
	 * Ademas, setea dentro del producto el valor de importeVenta obtenido de la relacion producto-costo para la fecha de venta.
	 * @param fecha
	 * @param idCat
	 * @param estado
	 * @return
	 */
	List<RelProductoCategoria> obtenerProductosCategoriaParaVenta(Date fecha, Short idCat, String estado);

	void asignarProductos(Categoria categoria, List<RelProductoCategoria> listaAgregados, List<RelProductoCategoria> listaModificados, List<RelProductoCategoria> listaEliminados) throws FechaDesdeException, ProductoInexistenteException;

	Date obtenerMinFechaDesde(Integer idProd);

	List<RelProductoCategoria> obtenerCategoriasProducto(Integer idProd, String estado);

	List<RelProductoCategoria> obtenerRelaciones(Integer idProd, Short idCat);
	
	RelProductoCategoria obtenerById(Integer idProdCat);

	List<RelProductoCategoria> obtenerRelaciones(String sortFieldName, String sortOrder);

	Map<Short, Integer> obtenerCantProductoVigentesXCat();

}
