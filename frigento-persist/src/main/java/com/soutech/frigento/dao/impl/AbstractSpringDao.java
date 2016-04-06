package com.soutech.frigento.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractSpringDao<T, I extends Serializable> {

	private Class<T> type;
	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	public AbstractSpringDao(){
		Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        type = (Class<T>) pt.getActualTypeArguments()[0];
	}
	
    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

	@SuppressWarnings("unchecked")
	@Transactional
	public I save(T obj) {
		return (I) getSession().save(obj);
	}

	@Transactional
	public void saveAll(Collection<T> entities) {
		for (T obj : entities)
			save(obj);
	}
	
	@Transactional
	public void update(T obj) {
		getSession().update(obj);
	}
	
	@Transactional
	public void saveOrUpdate(T obj) {
		getSession().saveOrUpdate(obj);
	}
	
	@Transactional
	public void delete(T obj) {
		getSession().delete(obj);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public T merge(T obj) {
		return (T) getSession().merge(obj);		
	}
	
	@Transactional
	public void deleteAll(Collection<T> entities) {
		for (T obj : entities)
			delete(obj);
	}
	
	public void flush() {
		getSession().flush();		
	}
		
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public T findById(I id) {
		if(id == null)	return null;
		return (T) getSession().get(type, id);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<T> findQuery(String query) {
		return getSession().createQuery(query).list();
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<T> findAll() {
		return getSession().createQuery(" from " + type.getName()).list();
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<T> findAll(String condicion) {
		return getSession().createQuery(" from " + type.getName()+" "+condicion).list();
	}
	
	@Transactional(readOnly=true)
	public List<T> findAll(String sortFieldName, String sortOrder) {
		StringBuilder query = new StringBuilder("from ");
		query.append(type.getName());
		query.append(" e ");
		if(sortFieldName != null){
			query.append("order by ");
			query.append(sortFieldName);
			query.append(" ");
			query.append(sortOrder);
		}
		return findQuery(query.toString());
	}


}