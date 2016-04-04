package com.soutech.frigento.web.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.soutech.frigento.service.ConfiguracionService;


public class ServletInitializer extends HttpServlet {
	
	private static final long serialVersionUID = 7996932562989631939L;
	private static Logger logger = Logger.getLogger(ServletInitializer.class);

	@Autowired
	public ConfiguracionService configuracionService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
		
		logger.debug("Se procede a inicializar BD en caso que corresponda");
		configuracionService.inicializarValoresBD();
		
	}
	
}