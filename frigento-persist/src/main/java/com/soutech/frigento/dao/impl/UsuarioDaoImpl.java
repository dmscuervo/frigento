package com.soutech.frigento.dao.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.soutech.frigento.dao.UsuarioDao;
import com.soutech.frigento.model.Usuario;

@Repository
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

}
