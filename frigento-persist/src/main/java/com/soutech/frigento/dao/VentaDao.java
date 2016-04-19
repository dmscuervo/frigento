package com.soutech.frigento.dao;
import java.util.List;

import com.soutech.frigento.model.Venta;

public interface VentaDao extends IDao<Venta, Integer> {

	List<Venta> findAll(Short[] estado, String sortFieldName, String sortOrder);

}
