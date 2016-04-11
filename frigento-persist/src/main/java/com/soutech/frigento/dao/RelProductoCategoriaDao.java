package com.soutech.frigento.dao;
import java.util.Date;
import java.util.List;

import com.soutech.frigento.model.RelProductoCategoria;

public interface RelProductoCategoriaDao extends IDao<RelProductoCategoria, Integer> {

	List<RelProductoCategoria> findAllByCategoria(Short idCat, String estado);

	RelProductoCategoria findRelacionActual(Integer idProd);

	RelProductoCategoria findActualByDupla(Short idCat, Integer idProd);

	Date findMinDate(Integer idProd);
}
