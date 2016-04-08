package com.soutech.frigento.dao;
import java.util.List;

import com.soutech.frigento.model.RelProductoCategoria;

public interface RelProductoCategoriaDao extends IDao<RelProductoCategoria, Integer> {

	List<RelProductoCategoria> findAllByCategoria(Short idCat);

	RelProductoCategoria findRelacionActual(Integer idProd);
}
