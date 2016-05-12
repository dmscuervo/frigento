package com.soutech.frigento.enums;

import java.util.NoSuchElementException;

import com.soutech.frigento.util.Constantes;

public enum IVAEnum {

	VENTIUNO(21f), DIEZYMEDIO(10.5f);
	
	private Float iva;
	
	IVAEnum(Float iva){
		this.iva = iva;
	}

	public Float getIva() {
		return iva;
	}

	public static IVAEnum valueOf(Float valor){
		if(valor != null){
			if(valor.equals(Constantes.IVA_21)){
				return IVAEnum.VENTIUNO;
			}
			if(valor.equals(Constantes.IVA_105)){
				return IVAEnum.DIEZYMEDIO;
			}
			throw new NoSuchElementException("Valor no definido: " + valor);
		}
		return null;
	}
}
