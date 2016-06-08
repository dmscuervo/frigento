package com.soutech.frigento.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.soutech.frigento.model.Usuario;

public class PasswordValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		boolean result = true;
		return result;
	}

	@Override
	public void validate(Object arg0, Errors arg1) {
		Usuario usu = (Usuario)arg0;
		if(usu.getPassword() == null || usu.getPassword().equals("")){
			arg1.rejectValue("password", "NotNull.registracionForm.password");
		}
		if(usu.getPasswordReingresada() == null || usu.getPasswordReingresada().equals("")){
			arg1.rejectValue("passwordReingresada", "NotNull.registracionForm.password");
		}
		//Si ya tienen errores salgo
		if(arg1.getFieldError("password") != null
				|| arg1.getFieldError("passwordReingresada") != null){
			return;
		}
		
		if(!usu.getPassword().equals(usu.getPasswordReingresada())){
			arg1.rejectValue("password", "registracion.error.password.diferente");
			arg1.rejectValue("passwordReingresada", "registracion.error.password.diferente");
			return;
		}
		
		if(usu.getPassword().length() < 6 || usu.getPassword().length() > 15){
			arg1.rejectValue("password", "registracion.error.password.tamanio");
		}
		if(usu.getPasswordReingresada().length() < 6 || usu.getPasswordReingresada().length() > 15){
			arg1.rejectValue("passwordReingresada", "registracion.error.password.tamanio");
		}
	}

}
