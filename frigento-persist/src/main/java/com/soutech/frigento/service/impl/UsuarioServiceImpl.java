package com.soutech.frigento.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soutech.frigento.dao.UsuarioDao;
import com.soutech.frigento.model.Usuario;
import com.soutech.frigento.service.UsuarioService;
import com.soutech.frigento.util.Utils;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
    UsuarioDao usuarioDao;

	@Override
	public void saveUsuario(Usuario usuario) {
		generarUserName(usuario);
		if(usuario.getCategoriaProducto().getId() == null){
			usuario.setCategoriaProducto(null);
		}
		usuarioDao.save(usuario);
	}

	@Override
	public Usuario obtenerUsuario(Integer id) {
		return usuarioDao.findById(id);
	}

	@Override
	public List<Usuario> obtenerUsuarios(Boolean estado, String[] sortFieldName, String[] sortOrder) {
		return usuarioDao.findAll(estado, sortFieldName, sortOrder);
	}

	@Override
	public void actualizarUsuario(Usuario usuario) {
		Usuario usuActual = usuarioDao.findById(usuario.getId());
		if(!usuario.getNombre().equals(usuActual.getNombre())
				|| usuario.getApellido() != null && !usuario.getApellido().equals(usuActual.getApellido())){
			//Cambio el nombre y/o apellido
			generarUserName(usuActual);
		}
		usuActual.setNombre(usuario.getNombre());
		usuActual.setApellido(usuario.getApellido());
		usuActual.setTelefono(usuario.getTelefono());
		usuActual.setCelular(usuario.getCelular());
		usuActual.setCalle(usuario.getCalle());
		usuActual.setAltura(usuario.getAltura());
		usuActual.setDepto(usuario.getDepto());
		usuActual.setEmail(usuario.getEmail());
		if(usuario.getCategoriaProducto().getId() == null){
			usuActual.setCategoriaProducto(null);
		}else{
			usuActual.setCategoriaProducto(usuario.getCategoriaProducto());
		}
		usuarioDao.update(usuActual);
		//Para visualizar correctamente el username generado
		usuario.setUsername(usuActual.getUsername());
	}

	@Override
	public Usuario eliminarUsuario(Integer usuarioId){
		Usuario usuario = usuarioDao.findById(usuarioId);
		usuario.setHabilitado(Boolean.FALSE);
		usuarioDao.update(usuario);
		return usuario;
	}
	
	@Override
	public Usuario reactivarUsuario(Integer usuarioId){
		Usuario usuario = usuarioDao.findById(usuarioId);
		usuario.setHabilitado(Boolean.TRUE);
		usuarioDao.update(usuario);
		return usuario;
	}
	
	private void generarUserName(Usuario usuario) {
		usuario.setNombre(Utils.controlEspacioMultiple(usuario.getNombre()));
		usuario.setApellido(Utils.controlEspacioMultiple(usuario.getApellido()));
		usuario.setUsername(Utils.generarUsername(usuario));
		Usuario usuarioExiste = usuarioDao.findByUserName(usuario.getUsername());
		int i = 1;
		while(usuarioExiste != null){
			usuario.setUsername(Utils.generarUsername(usuario).concat(String.valueOf(i)));
			usuarioExiste = usuarioDao.findByUserName(usuario.getUsername());
			i++;
		}
	}

}
