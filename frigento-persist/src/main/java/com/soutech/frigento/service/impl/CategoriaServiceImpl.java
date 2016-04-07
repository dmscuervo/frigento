package com.soutech.frigento.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soutech.frigento.dao.CategoriaDao;
import com.soutech.frigento.dao.RelProductoCategoriaDao;
import com.soutech.frigento.dao.UsuarioDao;
import com.soutech.frigento.exception.EntityExistException;
import com.soutech.frigento.model.Categoria;
import com.soutech.frigento.model.RelProductoCategoria;
import com.soutech.frigento.model.Usuario;
import com.soutech.frigento.service.CategoriaService;

@Service
public class CategoriaServiceImpl implements CategoriaService {

	@Autowired
    CategoriaDao categoriaDao;
	
	@Autowired
    UsuarioDao usuarioDao;
	
	@Autowired
	RelProductoCategoriaDao relProductoCategoriaDao;

	@Override
	public List<Categoria> obtenerCategorias() {
		return categoriaDao.findAll();
	}

	@Override
	public void saveCategoria(Categoria categoria) {
		categoriaDao.save(categoria);
	}

	@Override
	public Categoria obtenerCategoria(Short id) {
		return categoriaDao.findById(id);
	}

	@Override
	public List<Categoria> obtenerCategorias(String sortFieldName, String sortOrder) {
		return categoriaDao.findAll(sortFieldName, sortOrder);
	}

	@Override
	public void actualizarCategoria(Categoria categoria) {
		categoriaDao.update(categoria);
	}

	@Override
	public void eliminarCategoria(Categoria categoria) throws EntityExistException {
		List<Usuario> usuarios = usuarioDao.findAllByCategoriaProd(categoria.getId());
		if(usuarios != null && !usuarios.isEmpty()){
			StringBuilder usuariosStr = new StringBuilder("");
			for (int i = 0; i < usuarios.size(); i++) {
				Usuario usuario = usuarios.get(i);
				usuariosStr.append(usuario.getUsername());
				if(i == 9 && (i+1) < usuarios.size()){
					usuariosStr.append(" y mas... (total: " + usuarios.size() + ")");
					break;
				}
				if((i+1) != usuarios.size()){
					usuariosStr.append(", ");
				}
			}
			throw new EntityExistException("usuario", new Object[]{usuariosStr.toString()});
		}
		
		List<RelProductoCategoria> relaciones = relProductoCategoriaDao.findAllByCategoria(categoria.getId());
		if(relaciones != null){
			for (RelProductoCategoria relProductoCategoria : relaciones) {
				relProductoCategoriaDao.delete(relProductoCategoria);
			}
		}else{
			categoriaDao.delete(categoria);		
		}
	}
	
}
