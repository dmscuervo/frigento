package com.soutech.frigento.web.dto.reports;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import com.soutech.frigento.model.Promocion;

public class ColumnPrecioConIvaDTO extends ColumnExpressionReporteDTO {
	
	private static final long serialVersionUID = 2160812102519458280L;
	
	private BigDecimal iva;
	
	public ColumnPrecioConIvaDTO(Float iva){
		this.iva = new BigDecimal(iva).divide(new BigDecimal(100)).add(BigDecimal.ONE);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Object evaluate(Map fields, Map variables, Map parameters) {
		BigDecimal importeVenta = (BigDecimal) fields.get("importeVenta");
		if(fields.get("promocion") != null){
			Promocion promo = (Promocion)fields.get("promocion");
			BigDecimal descuento = importeVenta.multiply(promo.getDescuento()).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
			importeVenta = importeVenta.subtract(descuento);
		}
		return importeVenta.multiply(iva).setScale(2, RoundingMode.HALF_UP);
	}

}
