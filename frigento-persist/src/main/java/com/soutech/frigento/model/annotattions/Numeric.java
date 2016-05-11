package com.soutech.frigento.model.annotattions;

@java.lang.annotation.Target(java.lang.annotation.ElementType.FIELD)
@java.lang.annotation.Retention(value=java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
//@javax.validation.Constraint(validatedBy={})
public abstract @interface Numeric {
	
	//public abstract java.lang.String message() default "{javax.validation.constraints.Numeric.message}";
	
	public final String entero = "-?\\d";
	public final String entero_positivo = "\\d";
	public final String decimal = "-?\\d+(\\.\\d{1,2})?";
	public final String decimal_positivo = "\\d+(\\.\\d{1,2})?";
	public final String decimal_positivo_3decimal = "\\d+(\\.\\d{1,3})?";

	/**
	 * Valores permitidos: entero, decimal
	 */
	abstract String regexp();
}
