package com.soutech.frigento.dao;
import java.util.Date;
import java.util.List;

import com.soutech.frigento.model.RelProductoCategoria;

public interface RelProductoCategoriaDao extends IDao<RelProductoCategoria, Integer> {

	List<RelProductoCategoria> findAllByCategoria(Date fecha, Short idCat);
	
	List<RelProductoCategoria> findAllByCategoria(Short idCat, String estado, String[] sortFieldName, String[] sortOrder);

	List<RelProductoCategoria> findAllByProducto(Integer idProd, String estado);

	RelProductoCategoria findActualByDupla(Short idCat, Integer idProd);

	Date findMinDate(Integer idProd);

	List<RelProductoCategoria> findAllByDupla(Short idCat, Integer idProd);

	RelProductoCategoria findByDupla(Short id, Integer id2, Date fecha);

	List<RelProductoCategoria> findAllActuales();

}
