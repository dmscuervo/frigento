package com.soutech.frigento.dao;
import java.util.Date;
import java.util.List;

import com.soutech.frigento.model.ProductoCosto;

public interface ProductoCostoDao extends IDao<ProductoCosto, Integer> {

	ProductoCosto findCostoActual(Integer idProd);
	
	Date getMinFechaDesde(Integer idProd);

	ProductoCosto findByProductoFecha(Integer idProd, Date fecha);

	Date getMinFechaHasta(Integer idProd);

	List<ProductoCosto> findAll(String estadoRel, Date fecha, String sortFieldName, String sortOrder);

	List<ProductoCosto> findAllFechaDesdeEntre(Integer idProd, Date fechaIni, Date fechaFin, String sortFieldName, String sortOrder);

}
