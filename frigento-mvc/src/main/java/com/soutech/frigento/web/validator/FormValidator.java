package com.soutech.frigento.web.validator;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.soutech.frigento.model.annotattions.Numeric;
import com.soutech.frigento.util.PrinterStack;

public class FormValidator implements Validator {
	
	private Logger log = Logger.getLogger(this.getClass());

	private final String PATTER_ENTERO = "\\d";
	private final String PATTER_DECIMAL = "\\d+(\\.\\d{1,2})?";

	@Override
	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(Object obj, Errors error) {
//		try {
//			PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(obj.getClass()).getPropertyDescriptors();
//			for (PropertyDescriptor pd : propertyDescriptors) {
//				if(pd.isAnnotationPresent(Numeric.class)){
//					Object valor = null;
//					try {
//						Object invoke = pd.getReadMethod().invoke(obj);
//						
//					} catch (Exception e) {
//						log.info("No se pudo obtener el valor del campo " + pd.getName());
//						log.error(PrinterStack.getStackTraceAsString(e));
//						return;
//					}
//					
//			}
//		} catch (IntrospectionException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		Field[] declaredFields = obj.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			if(field.isAnnotationPresent(Numeric.class)){
				Object valor = null;
				if(!field.isAccessible()){
					try {
						Method getMethod = obj.getClass().getDeclaredMethod(obtenerGetter(field.getName()), field.getType());
						valor = getMethod.invoke(obj, (Object[])null);
					} catch (Exception e) {
						log.info("No se pudo obtener el valor del campo " + field.getName());
						log.error(PrinterStack.getStackTraceAsString(e));
						return;
					}
				}else{
					try {
						valor = field.get(obj);
					} catch (Exception e) {
						log.info("No se pudo obtener el valor del campo " + field.getName());
						log.error(PrinterStack.getStackTraceAsString(e));
						return;
					}
				}
				Numeric annotation = field.getAnnotation(Numeric.class);
				validar(error, field.getName(), valor, annotation);
			}
		}
		
		Method[] declaredMethods = obj.getClass().getDeclaredMethods();
		for (Method method : declaredMethods) {
			if(method.isAnnotationPresent(Numeric.class)){
				Object valor = null;
				if(method.getName().toLowerCase().startsWith("get")){
					try {
						valor = method.invoke(obj, (Object[])null);
					} catch (Exception e) {
						log.info("No se pudo obtener el valor del metodo " + method.getName());
						log.error(PrinterStack.getStackTraceAsString(e));
						return;
					}
				}else{
					log.error("Ubicacion de anotacion Numeric incorrecta. " + method.getName());
					return;
				}
				Numeric annotation = method.getAnnotation(Numeric.class);
				validar(error, method.getName(), valor, annotation);
			}
		}
	}

	private void validar(Errors arg1, String campo, Object valor, Numeric annotation) {
		Pattern pattern;
		Matcher matcher = null;
		if(annotation.tipo().equals(Numeric.entero)){
			pattern = Pattern.compile(PATTER_ENTERO);
			matcher = pattern.matcher(valor.toString());
		}else if(annotation.tipo().equals(Numeric.decimal)){
			pattern = Pattern.compile(PATTER_DECIMAL);
			matcher = pattern.matcher(valor.toString());
		}else {
			log.error("Valor de anotacion Numeric desconocida. " + annotation.tipo());
			return;
		}
		if (!matcher.matches()) {
			arg1.rejectValue(campo, "phone.incorrect");
		}
	}
	
	private String obtenerGetter(String field){
		return "get".concat(field.substring(0,1).toUpperCase().concat(field.substring(1, field.length())));
	}
	
}
