package com.soutech.frigento.dao;
import java.util.List;

import com.soutech.frigento.model.Promocion;

public interface PromocionDao extends IDao<Promocion, Integer>{

	public List<Promocion> obtenerPromociones(Boolean vigente, String sortFieldName, String sortOrder);
		
}
