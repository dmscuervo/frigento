package com.soutech.frigento.dao;
import java.util.Date;

import com.soutech.frigento.model.RelVentaProducto;

public interface RelVentaProductoDao extends IDao<RelVentaProducto, Long> {

	Date findMaxFechaNoAnulada(Integer id);
}
