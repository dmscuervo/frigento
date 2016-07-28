package com.soutech.frigento.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.dao.PromocionDao;
import com.soutech.frigento.model.Promocion;

@Repository
@Transactional(readOnly=true)
public class PromocionDaoImpl extends AbstractSpringDao<Promocion, Integer> implements PromocionDao {

}
