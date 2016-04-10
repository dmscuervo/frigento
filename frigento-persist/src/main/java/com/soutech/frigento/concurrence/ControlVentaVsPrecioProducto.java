package com.soutech.frigento.concurrence;

import com.soutech.frigento.exception.ControlConcurrenciaExcepcion;

/**
 * Esta clase controla que no se pueda realizar una nueva venta mientras se esta cambiando el precio de productos, o viceversa
 * @author Familia
 *
 */
public class ControlVentaVsPrecioProducto {

	private static Boolean FLAGS_CONTROL_PRECIO_VENTA = Boolean.FALSE;
	
	public static void aplicarFlags(Boolean usando, String path, String keyMessage){
		synchronized(FLAGS_CONTROL_PRECIO_VENTA){
			if(FLAGS_CONTROL_PRECIO_VENTA && usando){
				throw new ControlConcurrenciaExcepcion(path, keyMessage);
			}
			FLAGS_CONTROL_PRECIO_VENTA = usando;
		}
	}
	
	public static void aplicarFlags(Boolean usando, String path){
		aplicarFlags(usando, path, null);
	}
	
	public static void aplicarFlags(Boolean usando){
		aplicarFlags(usando, null, null);
	}
}
