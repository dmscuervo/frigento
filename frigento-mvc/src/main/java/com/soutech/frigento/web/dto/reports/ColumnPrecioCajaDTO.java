package com.soutech.frigento.web.dto.reports;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class ColumnPrecioCajaDTO extends ColumnExpressionReporteDTO {

	private static final long serialVersionUID = -8721143193568365369L;

	@Override
	@SuppressWarnings("rawtypes")
	public Object evaluate(Map fields, Map variables, Map parameters) {
		BigDecimal importeVenta = (BigDecimal) fields.get("importeVenta");
		Float pesoCaja = (Float) fields.get("pesoCaja");
        return importeVenta.multiply(new BigDecimal(pesoCaja)).setScale(2, RoundingMode.HALF_UP);
	}

}
