package com.soutech.frigento.dao;
import java.util.List;

import com.soutech.frigento.model.Producto;

public interface ProductoDao extends IDao<Producto, Integer> {

	Producto findByCodigo(Producto producto);

	Float obtenerStock(Integer idProd);

	List<Producto> findAll(String estado, String sortFieldName, String sortOrder);

	void reactivar(Producto producto);
}
