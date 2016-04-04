package com.soutech.frigento.dao;
import com.soutech.frigento.model.Usuario;

public interface UsuarioDao extends IDao<Usuario, Integer> {

	public Usuario findByUserName(String userName);
}
