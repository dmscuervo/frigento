package com.soutech.frigento.web.validator;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.soutech.frigento.model.annotattions.Numeric;
import com.soutech.frigento.util.PrinterStack;

@Component
public class FormValidator implements Validator {

	private Logger log = Logger.getLogger(this.getClass());

	@Override
	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(Object obj, Errors error) {
		PropertyDescriptor[] propertyDescriptors;
		try {
			propertyDescriptors = Introspector.getBeanInfo(obj.getClass()).getPropertyDescriptors();
		} catch (IntrospectionException e) {
			log.error(PrinterStack.getStackTraceAsString(e));
			return;
		}
		for (PropertyDescriptor pd : propertyDescriptors) {
			Method getter = pd.getReadMethod();
			Field field = null;
			try {
				field = obj.getClass().getDeclaredField(pd.getName());
			} catch (Exception e) {
				continue;
			}
			if(field.isAnnotationPresent(Numeric.class)){
				Object valor = null;
				try {
					valor = getter.invoke(obj);
				} catch (Exception e) {
					log.info("No se pudo obtener el valor del campo " + pd.getName());
					log.error(PrinterStack.getStackTraceAsString(e));
					return;
				}
				if(valor == null){
					continue;
				}
				Numeric annotation = field.getAnnotation(Numeric.class);
				validar(error, pd.getName(), valor, annotation);
			}
		}
	}

	private void validar(Errors arg1, String campo, Object valor, Numeric annotation) {
		Pattern pattern = Pattern.compile(annotation.regexp());
		Matcher matcher = pattern.matcher(valor.toString());
		if (!matcher.matches()) {
			arg1.rejectValue(campo, "Numeric.formato.".concat(campo));
		}
	}
}
