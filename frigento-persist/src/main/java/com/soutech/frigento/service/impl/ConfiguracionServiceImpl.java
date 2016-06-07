package com.soutech.frigento.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soutech.frigento.dao.EstadoDao;
import com.soutech.frigento.dao.LocalidadDao;
import com.soutech.frigento.dao.ParametroDao;
import com.soutech.frigento.dao.RolDao;
import com.soutech.frigento.dao.UsuarioDao;
import com.soutech.frigento.dto.LocalidadesDTO;
import com.soutech.frigento.dto.Parametros;
import com.soutech.frigento.enums.TipoEstadoEnum;
import com.soutech.frigento.model.Estado;
import com.soutech.frigento.model.Localidad;
import com.soutech.frigento.model.Parametro;
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
	@Autowired
	private ParametroDao parametroDao;
	@Autowired
	private Parametros parametros;
	@Autowired
	private LocalidadDao localidadDao;
	@Autowired
	private LocalidadesDTO localidades;
	
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
		
		List<Parametro> parametros = parametroDao.findAll();
		if(parametros.isEmpty()){
			log.info("No existen los parametros del sistema. Se procede a la inicializacion de valores.");
			Parametro p = new Parametro();
			p.setParametro(Parametros.NOMBRE_PROVEEDOR);
			p.setValor("Alimax");
			parametroDao.save(p);
			
			p = new Parametro();
			p.setParametro(Parametros.SMTP_GMAIL_PORT);
			p.setValor("465");
			parametroDao.save(p);
			
			p = new Parametro();
			p.setParametro(Parametros.SMTP_GMAIL_REMITENTE);
			p.setValor("info.frigento@gmail.com");
			parametroDao.save(p);
			
			p = new Parametro();
			p.setParametro(Parametros.SMTP_GMAIL_DESTINATARIOS_PEDIDOS);
			p.setValor("souzadie@gmail.com");
			parametroDao.save(p);
			
			p = new Parametro();
			p.setParametro(Parametros.SMTP_GMAIL_DESTINATARIOS_CC_PEDIDOS);
			p.setValor("macguirelorena@gmail.com");
			parametroDao.save(p);
			
			p = new Parametro();
			p.setParametro(Parametros.SMTP_GMAIL_DESTINATARIOS_CC_VENTAS);
			p.setValor("macguirelorena@gmail.com");
			parametroDao.save(p);
			
			p = new Parametro();
			p.setParametro(Parametros.TIME_ZONE_BUENOS_AIRES);
			p.setValor("America/Buenos_Aires");
			parametroDao.save(p);
			
			p = new Parametro();
			p.setParametro(Parametros.TOLERANCIA_GRAMOS_PROMOCION_VTA);
			p.setValor("0.100");
			parametroDao.save(p);
			
			p = new Parametro();
			p.setParametro(Parametros.MAX_SIZE_IMAGEN_PRODUCTO_BYTES);
			p.setValor("1000000");
			parametroDao.save(p);
		}
		
		log.debug("Se finalizado la inicializacion de datos.");
	}

	@Override
	public void cargarParametros() {
		List<Parametro> findAll = parametroDao.findAll();
		Map<String, String> valores = new HashMap<String, String>();
		for (Parametro parametro : findAll) {
			valores.put(parametro.getParametro(), parametro.getValor());
		}
		//Parametros fijos
		valores.put(Parametros.SMTP_GMAIL_PASSWORD, "4l1m3nt0s");
		parametros.setParametros(valores);
	}

	@Override
	public void cargarLocalidades() {
		List<Localidad> findAll = localidadDao.findAll();
		Map<Short, String> valores = new HashMap<Short, String>();
		for (Localidad localidad : findAll) {
			valores.put(localidad.getId(), localidad.getNombre());
		}
		localidades.setLocalidades(valores);
	}
}
