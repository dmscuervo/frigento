package com.soutech.frigento.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.dao.RolDao;
import com.soutech.frigento.model.Rol;

@Repository
@Transactional(readOnly=true)
public class RolDaoImpl extends AbstractSpringDao<Rol, Integer> implements RolDao {

}