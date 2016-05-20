package com.soutech.frigento.dao;
import java.util.List;

import com.soutech.frigento.model.Pedido;

public interface PedidoDao extends IDao<Pedido, Integer> {

	List<Pedido> findAll(Short[] estado, String sortFieldName, String sortOrder);

	List<Pedido> findAllSinPagar(Short[] estado, String sortFieldName, String sortOrder);
}
