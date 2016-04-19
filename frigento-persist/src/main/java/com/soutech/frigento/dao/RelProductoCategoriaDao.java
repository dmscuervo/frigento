package com.soutech.frigento.dao;
import java.util.Date;
import java.util.List;

import com.soutech.frigento.model.RelProductoCategoria;

public interface RelProductoCategoriaDao extends IDao<RelProductoCategoria, Integer> {

	List<RelProductoCategoria> findAllByCategoria(Date fecha, Short idCat, String estado);

	List<RelProductoCategoria> findAllByProducto(Integer idProd, String estado);

	RelProductoCategoria findActualByDupla(Short idCat, Integer idProd);

	Date findMinDate(Integer idProd);

}
