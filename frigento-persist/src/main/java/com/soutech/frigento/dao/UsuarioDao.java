package com.soutech.frigento.dao;
import java.util.List;

import com.soutech.frigento.model.Usuario;

public interface UsuarioDao extends IDao<Usuario, Integer> {

	public Usuario findByUserName(String userName);
	
	public List<Usuario> findAllByCategoriaProd(Short idCat);

	public List<Usuario> findAll(Boolean estado, String[] sortFieldName, String[] sortOrder);

}
