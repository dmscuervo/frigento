package com.soutech.frigento.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.dao.EstadoDao;
import com.soutech.frigento.dao.RolDao;
import com.soutech.frigento.dao.UsuarioDao;
import com.soutech.frigento.enums.TipoEstadoEnum;
import com.soutech.frigento.model.Estado;
import com.soutech.frigento.model.Rol;
import com.soutech.frigento.model.Usuario;
import com.soutech.frigento.service.ConfiguracionService;

@Service
public class ConfiguracionServiceImpl implements ConfiguracionService {

	private final Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private UsuarioDao usuarioDao;
	
	@Autowired
	private RolDao rolDao;
	
	@Autowired
	private EstadoDao estadoDao;
	
	@Override
	@Transactional
	public void inicializarValoresBD() {
		log.debug("Se comienza la inicializacion de datos.");
		List<Estado> estados = estadoDao.findAll();
		if(estados.isEmpty()){
			log.info("No existen estados de pedidos/ventas. Se procede a la inicializacion de valores.");
			Estado estado = new Estado();
			estado.setDescripcion("Pendiente");
			estado.setTipo(TipoEstadoEnum.A);
			estadoDao.save(estado);
			
			estado = new Estado();
			estado.setDescripcion("Confirmado");
			estado.setTipo(TipoEstadoEnum.A);
			estadoDao.save(estado);
			
			estado = new Estado();
			estado.setDescripcion("Cumplido");
			estado.setTipo(TipoEstadoEnum.A);
			estadoDao.save(estado);
			
			estado = new Estado();
			estado.setDescripcion("Anulado");
			estado.setTipo(TipoEstadoEnum.A);
			estadoDao.save(estado);
		}
		
		Usuario admin = usuarioDao.findByUserName("admin");
		if(admin == null){
			log.info("No existe el usuario admin. Se procede a su creacion.");
			admin = new Usuario();
			admin.setUsername("admin");
			admin.setPassword("3b7786ad2afd455210a81c4290182b17ec0d052b5213b28f9afc8c8c7a3a9b21");
			admin.setNombre("Administrador");
			admin.setEsAdmin(Boolean.TRUE);
			admin.setHabilitado(Boolean.TRUE);
			usuarioDao.save(admin);
			
			Rol rol = new Rol();
			rol.setRol(Rol.ROLE_ADMIN);
			rol.setUserName(admin.getUsername());
			rolDao.save(rol);
		}
		log.debug("Se finalizado la inicializacion de datos.");
	}

}
