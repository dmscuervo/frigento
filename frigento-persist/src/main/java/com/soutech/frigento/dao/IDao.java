package com.soutech.frigento.dao;

import java.util.Collection;
import java.util.List;

public interface IDao<T, I> {

	void update(T entity);

	I save(T entity);

	void saveOrUpdate(T entity);

	void delete(T entity);

	void saveAll(Collection<T> listEntity);

	T findById(I idEntity);

	List<T> findAll();

	List<T> findAll(String condicion);
}
