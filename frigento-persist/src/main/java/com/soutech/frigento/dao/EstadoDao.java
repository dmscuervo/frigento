package com.soutech.frigento.dao;
import java.util.List;

import com.soutech.frigento.enums.TipoEstadoEnum;
import com.soutech.frigento.model.Estado;

public interface EstadoDao extends IDao<Estado, Short> {

	List<Estado> findAllByTipo(TipoEstadoEnum[] tipoEstadoEnums);
}
