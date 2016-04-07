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

}
