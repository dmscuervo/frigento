package com.soutech.frigento.web.handler;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.soutech.frigento.exception.ControlConcurrenciaExcepcion;
import com.soutech.frigento.util.PrinterStack;

@ControllerAdvice
public class HandlerException {

	private static final Logger logger = Logger.getLogger(HandlerException.class);
	
	@Autowired
	private MessageSource messageSource;
    
	@ExceptionHandler(ControlConcurrenciaExcepcion.class)
    public String handleControlConcurrenciaException(HttpServletRequest request, ControlConcurrenciaExcepcion ex){
		logger.info("Control de concurrencia aplicado.");
		return ex.getPath().concat("&informar=".concat(messageSource.getMessage(ex.getKeyMessage(), null, Locale.getDefault())));
	}
	

	@ExceptionHandler(HttpSessionRequiredException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason="The session has expired")
	public String handleSessionExpired(){		
	  return "sessionExpired";
	}
	
    @ExceptionHandler(Exception.class)
    public String handleSQLException(HttpServletRequest request, Exception ex){
        logger.info("SQLException Occured:: URL="+request.getRequestURL());
        
        request.setAttribute("msgTitle", "Error Inesperado");
        if(ex.getMessage() != null){
        	request.setAttribute("msgResult", "Ocurrio un error no conteplado. Contactese con el administrador.<br>".concat(ex.getMessage()));
        }else{
        	request.setAttribute("msgResult", "Ocurrio un error no conteplado. Contactese con el administrador.<br>");
        }
        logger.error(PrinterStack.getStackTraceAsString(ex));
        
        return "generic/mensajeException";
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(HttpServletRequest request, Exception ex){
        logger.info("Acceso denegado: URL="+request.getRequestURL());
        
        request.setAttribute("msgTitle", "Acceso Denegado");
        request.setAttribute("msgResult", "No dispone de permisos para utilizar esta función.<br>".concat(ex.getMessage()));
        
        return "generic/mensajeException";
    }
     
//    @ResponseStatus(value=HttpStatus.NOT_FOUND, reason="IOException occured")
//    @ExceptionHandler(IOException.class)
//    public void handleIOException(){
//        logger.error("IOException handler executed");
//        //returning 404 error code
//    }

}
