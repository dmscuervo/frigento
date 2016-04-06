package com.soutech.frigento.model.annotattions;

@java.lang.annotation.Target(java.lang.annotation.ElementType.FIELD)
@java.lang.annotation.Retention(value=java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
//@javax.validation.Constraint(validatedBy={})
public abstract @interface Numeric {
	
	//public abstract java.lang.String message() default "{javax.validation.constraints.Numeric.message}";
	
	public final String entero = "entero";
	public final String decimal = "decimal";

	/**
	 * Valores permitidos: entero, decimal
	 */
	abstract String tipo() default "entero";
}
