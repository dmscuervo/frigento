package com.soutech.frigento.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.dao.LocalidadDao;
import com.soutech.frigento.model.Localidad;

@Repository
@Transactional(readOnly=true)
public class LocalidadDaoImpl extends AbstractSpringDao<Localidad, Short> implements LocalidadDao {

}