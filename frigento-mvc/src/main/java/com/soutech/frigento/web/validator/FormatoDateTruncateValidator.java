package com.soutech.frigento.web.validator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.propertyeditors.CustomDateEditor;

public class FormatoDateTruncateValidator extends CustomDateEditor {
	
	private final SimpleDateFormat sdf = new SimpleDateFormat(":ss.SSS");

	public FormatoDateTruncateValidator(DateFormat dateFormat, boolean allowEmpty, int exactDateLength) {
		super(dateFormat, allowEmpty, exactDateLength);
	}

	public FormatoDateTruncateValidator(DateFormat dateFormat, boolean allowEmpty) {
		super(dateFormat, allowEmpty);
	}

	@Override
	public String getAsText() {
		String valor = super.getAsText();
		if(valor != null && valor.length() == 23){
			//Le quito los segundos y milisegundos para su visualizacion
			valor = valor.substring(0, super.getAsText().length()-7);
		}
		return valor;
	}

	@Override
	public void setAsText(String arg0) throws IllegalArgumentException {
		if(arg0 != null && arg0.length() == 16){
			//Le agrego los segundos y milisegundos
			arg0 = arg0.concat(sdf.format(Calendar.getInstance().getTime()));
		}
		super.setAsText(arg0);
	}

}
