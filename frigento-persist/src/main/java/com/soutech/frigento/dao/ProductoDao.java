package com.soutech.frigento.dao;
import com.soutech.frigento.model.Producto;

public interface ProductoDao extends IDao<Producto, Integer> {

	Producto findByCodigo(Producto producto);
}
