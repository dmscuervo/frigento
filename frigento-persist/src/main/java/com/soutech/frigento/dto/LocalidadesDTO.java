package com.soutech.frigento.dto;

import java.util.Iterator;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.soutech.frigento.model.Localidad;

@Component
public class LocalidadesDTO {
	
	private static Map<Short, String> localidades;

	public static Map<Short, String> getLocalidades() {
		return localidades;
	}

	public void setLocalidades(Map<Short, String> localidades) {
		LocalidadesDTO.localidades = localidades;
	}
	
	public static Localidad getLocalidadDefault(){
		if(localidades.isEmpty()){
			return null;
		}
		Localidad localidad = new Localidad();
		localidad.setId(new Short("0"));
		localidad.setNombre(localidades.get(localidad.getId()));
		return localidad;
	}

	public static Localidad getLocalidad(String localidad) {
		Localidad result = null;
		if(localidades.containsValue(localidad)){
			Iterator<Short> iterator = localidades.keySet().iterator();
			while(iterator.hasNext()){
				Short key = iterator.next();
				if(localidades.get(key).equals(localidad)){
					result = new Localidad();
					result.setId(key);
					result.setNombre(localidades.get(key));
					break;
				}
			}
		}
		return result;
	}
}
