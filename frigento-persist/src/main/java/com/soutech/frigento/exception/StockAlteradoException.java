package com.soutech.frigento.exception;

import com.soutech.frigento.model.Producto;

public class StockAlteradoException extends Exception {

	private static final long serialVersionUID = 6484904884216904772L;
	private Producto productoRecargado;
	
	public StockAlteradoException(Producto producto){
		this.productoRecargado = producto;
	}

	public Producto getProductoRecargado() {
		return productoRecargado;
	}
	
}
