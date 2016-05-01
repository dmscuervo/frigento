package com.soutech.frigento.web.dto.reports;

import java.util.Map;

public abstract class ColumnExpressionReporteDTO extends ColumnReporteDTO {

	@SuppressWarnings("rawtypes")
	public abstract Object evaluate(Map fields, Map variables, Map parameters);
}
