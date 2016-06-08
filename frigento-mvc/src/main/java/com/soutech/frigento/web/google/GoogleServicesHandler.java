package com.soutech.frigento.web.google;

import java.net.URL;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class GoogleServicesHandler {
	

	public final String urlGeocode = "https://maps.googleapis.com/maps/api/geocode/json?address={0}+{1}+ciudad+de+buenos+aires+argentina&key=AIzaSyB_35tOlw1KJnnOiSF2GOWtlEJuvbHU8Eg";
	public final String urlDistance = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=Gavilan+1950+ciudad+de+buenos+aires&destinations={0}+{1}+ciudad+de+buenos+aires&key=AIzaSyB_35tOlw1KJnnOiSF2GOWtlEJuvbHU8Eg";
	
	private final String type_localidad = "locality";
	private final String type_barrio = "sublocality";
	
	Logger log = Logger.getLogger(this.getClass());
	
	public <T> T invocarURL(String url, Class<T> claz) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		T result = mapper.readValue(new URL(url), claz);
        return result;
	}
	
	public Boolean esCABA(GoogleGeocode geocode){
		Boolean esCABA = null;
		if(!geocode.getStatus().equals("OK")){
			if(geocode.getStatus().equals("OVER_QUERY_LIMIT")){
				log.info("Se excedio la cuota de consulta a google geocode");
			}
			return Boolean.FALSE;
		}
		if(geocode.getResults().length == 0){
			return Boolean.FALSE;
		}
		Address[] address_components = geocode.getResults()[0].getAddress_components();
		for (int i = 0; i < address_components.length; i++) {
			Address address = address_components[i];
			String[] types = address.getTypes();
			for (int j = 0; j < types.length; j++) {
				if(types[j].equals(type_localidad)){
					esCABA = address.getShort_name().toUpperCase().equals("CABA");
					break;
				}
			}
			if(esCABA != null){
				break;
			}
		}
		return esCABA;
	}
	
	public String getLocalidad(GoogleGeocode geocode){
		String localidad = null;
		if(!geocode.getStatus().equals("OK")){
			if(geocode.getStatus().equals("OVER_QUERY_LIMIT")){
				log.info("Se excedio la cuota de consulta a google geocode");
			}
			return localidad;
		}
		if(geocode.getResults().length == 0){
			return localidad;
		}
		Address[] address_components = geocode.getResults()[0].getAddress_components();
		for (int i = 0; i < address_components.length; i++) {
			Address address = address_components[i];
			String[] types = address.getTypes();
			for (int j = 0; j < types.length; j++) {
				if(types[j].equals(type_barrio)){
					localidad = address.getLong_name();
					break;
				}
			}
			if(localidad != null){
				break;
			}
		}
		return localidad;
	}

	public Integer getDistancia(GoogleDistance distance) {
		Integer metros = null;
		if(!distance.getStatus().equals("OK")){
			return metros;
		}
		if(distance.getRows().length == 0){
			return metros;
		}
		Element[] elements = distance.getRows()[0].getElements();
		if(elements.length == 0){
			return metros;
		}
		
		String value = elements[0].getDistance().getValue();
		try {
			metros = Integer.parseInt(value);
		} catch (NumberFormatException e) {
			log.error("La cantidad de metros no es entera. (" + value + ")");
			log.info("Dirección destino: " + distance.getDestination_addresses());
			return metros;
		}

		return metros;
	}

	public Boolean esDirecciónUnica(GoogleGeocode geocode) {
		Boolean esUnica = Boolean.TRUE;
		if(!geocode.getStatus().equals("OK")){
			if(geocode.getStatus().equals("OVER_QUERY_LIMIT")){
				log.info("Se excedio la cuota de consulta a google geocode");
			}
			return Boolean.FALSE;
		}
		if(geocode.getResults().length != 1){
			esUnica = Boolean.FALSE;
		}
		return esUnica;
	}
}