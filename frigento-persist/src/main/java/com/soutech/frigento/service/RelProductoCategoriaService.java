package com.soutech.frigento.service;

import java.util.Date;
import java.util.List;

import com.soutech.frigento.exception.FechaDesdeException;
import com.soutech.frigento.model.Categoria;
import com.soutech.frigento.model.RelProductoCategoria;

public interface RelProductoCategoriaService {

	List<RelProductoCategoria> obtenerProductosCategoria(Short idCat, String estado);

	void asignarProductos(Categoria categoria, List<RelProductoCategoria> relaciones) throws FechaDesdeException;

	Date obtenerMinFechaDesde(Integer idProd);

	List<RelProductoCategoria> obtenerCategoriasProducto(Integer idProd, String estado);

}
