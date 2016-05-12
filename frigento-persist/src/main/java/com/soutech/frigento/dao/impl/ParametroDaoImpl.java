package com.soutech.frigento.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.dao.ParametroDao;
import com.soutech.frigento.model.Parametro;

@Repository
@Transactional(readOnly=true)
public class ParametroDaoImpl extends AbstractSpringDao<Parametro, Integer> implements ParametroDao {

}