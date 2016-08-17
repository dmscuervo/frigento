package com.soutech.frigento.web.dto.reports;

import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;

import com.soutech.frigento.model.Promocion;

public class ColumnDescripcionPromoDTO extends ColumnExpressionReporteDTO {

	private static final long serialVersionUID = -243120026630947229L;
	private MessageSource message;
	private String campo;
	
	public ColumnDescripcionPromoDTO(MessageSource messageSource, String property){
		this.message = messageSource;
		this.campo = property;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Object evaluate(Map fields, Map variables, Map parameters) {
		String descripcion = (String) fields.get(campo);
		String promoStr = "";
		if(fields.get("promocion") != null){
			Promocion promo = (Promocion)fields.get("promocion");
			promoStr = message.getMessage("venta.promocion", new Object[]{promo.getCantidadMinima()}, Locale.getDefault());
		}
        return promoStr.concat(descripcion);
	}

}
