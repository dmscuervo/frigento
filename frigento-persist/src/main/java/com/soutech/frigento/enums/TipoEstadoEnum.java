package com.soutech.frigento.enums;

public enum TipoEstadoEnum {

	A("Ambos"), P("Pedido"), V("Venta");
	
	private String desc;
	
	TipoEstadoEnum(String desc){
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
	
}
