package com.soutech.frigento.service;

import java.util.List;

import com.soutech.frigento.model.Usuario;

public interface UsuarioService {

	void saveUsuario(Usuario usuario);

	Usuario obtenerUsuario(Integer id);

	List<Usuario> obtenerUsuarios(Boolean estado, String[] sortFieldName, String[] sortOrder);

	void actualizarUsuario(Usuario usuario);

	Usuario eliminarUsuario(Integer usuarioId);

	public Usuario reactivarUsuario(Integer usuarioId);

	List<Usuario> obtenerUsuariosConCategoria(Boolean estado, String[] sortFieldName, String[] sortOrder);
}
