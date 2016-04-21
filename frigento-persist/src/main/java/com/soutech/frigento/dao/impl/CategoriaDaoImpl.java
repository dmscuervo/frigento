package com.soutech.frigento.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.dao.CategoriaDao;
import com.soutech.frigento.model.Categoria;

@Repository
@Transactional(readOnly=true)
public class CategoriaDaoImpl extends AbstractSpringDao<Categoria, Short> implements CategoriaDao {

}
