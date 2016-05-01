package com.soutech.frigento.web.dto.reports;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class ColumnPrecioCajaDTO extends ColumnExpressionReporteDTO {

	@Override
	@SuppressWarnings("rawtypes")
	public Object evaluate(Map fields, Map variables, Map parameters) {
		BigDecimal precioVenta = (BigDecimal) fields.get("precioVenta");
		Float pesoCaja = (Float) fields.get("pesoCaja");
        return precioVenta.multiply(new BigDecimal(pesoCaja)).setScale(2, RoundingMode.HALF_UP);
	}

}
