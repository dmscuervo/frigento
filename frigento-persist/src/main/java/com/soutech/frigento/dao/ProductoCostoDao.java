package com.soutech.frigento.dao;
import com.soutech.frigento.model.ProductoCosto;

public interface ProductoCostoDao extends IDao<ProductoCosto, Integer> {

	ProductoCosto findCostoActual(Integer idProd);

	ProductoCosto findActualByProducto(Integer idProd);
}
