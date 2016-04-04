package com.soutech.frigento.web.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;


public class ServletInitializer extends HttpServlet {
	
	private static final long serialVersionUID = 7996932562989631939L;
	private static Logger logger = Logger.getLogger(ServletInitializer.class);

	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		//SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
		System.out.println("");
	}
	
}