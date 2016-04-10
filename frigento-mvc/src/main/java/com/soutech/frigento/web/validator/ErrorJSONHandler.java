package com.soutech.frigento.web.validator;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soutech.frigento.util.PrinterStack;

@Component
public class ErrorJSONHandler {
	
	Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private MessageSource messageSource;

	public String getJSON(Object errorView, BindingResult bindingResult){
		String json = "";
		List<FieldError> errores = bindingResult.getFieldErrors();
		for (FieldError error : errores) {
			String field = error.getField();
			String keyMsg = error.getObjectName().concat(".").concat(error.getCode()).concat(".").concat(error.getField());
			setValue(errorView, field, keyMsg);
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorView);
		} catch (JsonProcessingException e) {
			log.info("No se pudo convertir a json.");
			log.error(PrinterStack.getStackTraceAsString(e));
		}
		return json;
	}

	private void setValue(Object errorView, String campo, String keyMsg) {
		PropertyDescriptor[] propertyDescriptors;
		try {
			propertyDescriptors = Introspector.getBeanInfo(errorView.getClass()).getPropertyDescriptors();
		} catch (IntrospectionException e) {
			log.error(PrinterStack.getStackTraceAsString(e));
			return;
		}
		Boolean alMenosUno = Boolean.FALSE;
		log.debug("Se procede a setear campos para validacion de errores.");
		log.debug("Campo view: " + campo);
		for (PropertyDescriptor pd : propertyDescriptors) {
			log.debug("Campo obj: " + pd.getName());
			if(!pd.getName().equals(campo)){
				continue;
			}
			alMenosUno = Boolean.TRUE;
			Method setter = pd.getWriteMethod();
			try {
				setter.invoke(errorView, messageSource.getMessage(keyMsg, null, Locale.getDefault()));
			} catch (Exception e) {
				log.info("No se pudo setear el valor del campo " + pd.getName());
				log.error(PrinterStack.getStackTraceAsString(e));
				return;
			}
		}
		if(!alMenosUno){
			log.error("No se establecio ningun valor sobre los campos definidos. No se visualizaran errores en la pantalla.");
		}
	}
}
