package com.soutech.frigento.dao;
import java.util.Date;
import java.util.List;

import com.soutech.frigento.model.RelVentaProducto;

public interface RelVentaProductoDao extends IDao<RelVentaProducto, Long> {

	Date findMaxFechaNoAnulada(Integer id);
	List<RelVentaProducto> findAllNoAnulada(Integer prodId, Short catId, Date fechaIni, Date fechaFin);
	Date obtenerFechaPrimerVentaNoAnulada(Integer prodId, Short catId, Date fechaIni, Date fechaFin);
}
