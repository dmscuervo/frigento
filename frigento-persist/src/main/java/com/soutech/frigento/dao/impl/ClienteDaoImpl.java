package com.soutech.frigento.dao.impl;

import org.springframework.stereotype.Repository;

import com.soutech.frigento.dao.ClienteDao;
import com.soutech.frigento.model.Cliente;

@Repository
public class ClienteDaoImpl extends AbstractSpringDao<Cliente, Integer> implements ClienteDao {

}
