package com.soutech.frigento.concurrence;

import com.soutech.frigento.exception.ControlConcurrenciaExcepcion;

/**
 * Esta clase controla que no se pueda realizar una nuevo pedido mientras se esta cambiando el costo de productos, o viceversa
 * @author Familia
 *
 */
public class ControlPedidoVsCostoProducto {

	private static Boolean FLAGS_CONTROL_COSTO_PEDIDO = Boolean.FALSE;
	
	public static void aplicarFlags(Boolean usando, String path, String keyMessage){
		synchronized(FLAGS_CONTROL_COSTO_PEDIDO){
			if(FLAGS_CONTROL_COSTO_PEDIDO && usando){
				throw new ControlConcurrenciaExcepcion(path, keyMessage);
			}
			FLAGS_CONTROL_COSTO_PEDIDO = usando;
		}
	}
	
	public static void aplicarFlags(Boolean usando, String path){
		aplicarFlags(usando, path, null);
	}
	
	public static void aplicarFlags(Boolean usando){
		aplicarFlags(usando, null, null);
	}
}
