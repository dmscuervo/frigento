package com.soutech.frigento.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.dao.UsuarioDao;
import com.soutech.frigento.model.Usuario;

@Repository
@Transactional(readOnly=true)
public class UsuarioDaoImpl extends AbstractSpringDao<Usuario, Integer> implements UsuarioDao {

	@Override
	public Usuario findByUserName(String userName) {
		StringBuilder hql = new StringBuilder("from ");
		hql.append(Usuario.class.getCanonicalName());
		hql.append(" u where u.username = :un");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("un", userName);
		return (Usuario) query.uniqueResult();
	}
	
	@Override
	public Usuario findByEmail(String email) {
		StringBuilder hql = new StringBuilder("from ");
		hql.append(Usuario.class.getCanonicalName());
		hql.append(" u where lower(u.email) = :email");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("email", email.toLowerCase());
		return (Usuario) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> findAllByCategoriaProd(Short idCat) {
		StringBuilder hql = new StringBuilder("from ");
		hql.append(Usuario.class.getCanonicalName());
		hql.append(" u where u.categoriaProducto.id = :catId");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("catId", idCat);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> findAll(Boolean estado, String[] sortFieldName, String[] sortOrder) {
		StringBuilder hql = new StringBuilder("from ");
		hql.append(Usuario.class.getCanonicalName());
		hql.append(" u ");
		if(estado != null){
			hql.append(" where u.habilitado = :estado ");
		}
		if(sortFieldName != null){
			hql.append("order by u.");
			for (int i = 0; i < sortFieldName.length; i++) {
				String sfn = sortFieldName[i];
				String so;
				try {
					so = sortOrder[i];
				} catch (Exception e) {
					so = "asc";
				}
				hql.append(sfn);
				hql.append(" ");
				hql.append(so);
				hql.append(", u.");
			}
			hql.replace(hql.length()-4, hql.length(), "");
		}
		Query query = getSession().createQuery(hql.toString());
		if(estado != null){
			query.setParameter("estado", estado);
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> findAllConCategoria(Boolean estado, String[] sortFieldName, String[] sortOrder) {
		StringBuilder hql = new StringBuilder("from ");
		hql.append(Usuario.class.getCanonicalName());
		hql.append(" u ");
		hql.append(" where u.categoriaProducto is not null ");
		if(estado != null){
			hql.append("and u.habilitado = :estado ");
		}
		if(sortFieldName != null){
			hql.append("order by u.");
			for (int i = 0; i < sortFieldName.length; i++) {
				String sfn = sortFieldName[i];
				String so;
				try {
					so = sortOrder[i];
				} catch (Exception e) {
					so = "asc";
				}
				hql.append(sfn);
				hql.append(" ");
				hql.append(so);
				hql.append(", u.");
			}
			hql.replace(hql.length()-4, hql.length(), "");
		}
		Query query = getSession().createQuery(hql.toString());
		if(estado != null){
			query.setParameter("estado", estado);
		}
		return query.list();
	}

}
