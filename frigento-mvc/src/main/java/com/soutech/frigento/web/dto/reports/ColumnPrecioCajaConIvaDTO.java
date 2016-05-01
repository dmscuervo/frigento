package com.soutech.frigento.web.dto.reports;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class ColumnPrecioCajaConIvaDTO extends ColumnExpressionReporteDTO {

	private BigDecimal iva;
	
	public ColumnPrecioCajaConIvaDTO(Float iva){
		this.iva = new BigDecimal(iva).divide(new BigDecimal(100)).add(BigDecimal.ONE);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Object evaluate(Map fields, Map variables, Map parameters) {
		BigDecimal precioVenta = (BigDecimal) fields.get("precioVenta");
		Float pesoCaja = (Float) fields.get("pesoCaja");
        return precioVenta.multiply(new BigDecimal(pesoCaja)).multiply(iva).setScale(2, RoundingMode.HALF_UP);
	}

}
