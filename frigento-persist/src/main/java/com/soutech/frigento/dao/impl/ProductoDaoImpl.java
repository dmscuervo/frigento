package com.soutech.frigento.dao.impl;

import org.springframework.stereotype.Repository;

import com.soutech.frigento.dao.ProductoDao;
import com.soutech.frigento.model.Producto;

@Repository
public class ProductoDaoImpl extends AbstractSpringDao<Producto, Integer> implements ProductoDao {

}
