package com.soutech.frigento.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.soutech.frigento.model.Pedido;
import com.soutech.frigento.model.Usuario;
import com.soutech.frigento.model.Venta;

public class Utils {

	public static final SimpleDateFormat SDF_DDMMYYYY_HHMM = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	
	public static String formatDate(Date fecha, SimpleDateFormat sdf){
		sdf.setLenient(Boolean.FALSE);
		return sdf.format(fecha);
	}

	public static String aTextoConCeroIzqSegunCantDigitos(int i, int digits) {
		String result = String.valueOf(i);
		while(result.length() < digits){
			result = "0".concat(result);
		}
		return result;
	}

	public static String generarNroRemito(Pedido pedido) {
		StringBuilder nro = new StringBuilder("S");
		nro.append(aTextoConCeroIzqSegunCantDigitos(pedido.getVersion().intValue(), 2));
		nro.append("-");
		nro.append(aTextoConCeroIzqSegunCantDigitos(pedido.getId(), 8));
		return nro.toString();
	}
	
	public static String generarNroRemito(Venta venta) {
		StringBuilder nro = new StringBuilder("V01-");
		nro.append(aTextoConCeroIzqSegunCantDigitos(venta.getId(), 8));
		return nro.toString();
	}

	public static String generarUsername(Usuario usuario) {
		String nick;
		if(usuario.getApellido() == null || usuario.getApellido().equals("")){
			nick = usuario.getNombre().split(" ")[0].toLowerCase();
		}else{
			nick = usuario.getNombre().split(" ")[0].toLowerCase().substring(0, 1).concat(usuario.getApellido().replaceAll(" ", "").toLowerCase());
		}
		return nick;
	}
	
	public static String controlEspacioMultiple(String dato) {
		String[] split = dato.trim().split(" ");
		StringBuilder p = new StringBuilder();
		for (int i = 0; i < split.length; i++) {
			if(!split[i].equals("")){
				p.append(split[i]);
				p.append(" ");
			}
		}
		if(p.length()>1){
			p.replace(p.length()-1, p.length(), "");
		}
		return p.toString();
	}

	/**
	 * Determina si fecha1 es mayor e igual que fecha2
	 * @param fecha1
	 * @param fecha2
	 * @return
	 */
	public static Boolean esMayorIgual(Date fecha1, Date fecha2) {
		if(fecha1.equals(fecha2) || fecha1.after(fecha2)){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	/**
	 * Determina si fecha1 es menor que fecha2
	 * @param fecha1
	 * @param fecha2
	 * @return
	 */
	public static Boolean esMenor(Date fecha1, Date fecha2) {
		if(fecha1.before(fecha2)){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	/**
	 * Determina si fecha1 esta dentro de 
	 * @param fecha1
	 * @param fecha2
	 * @return
	 */
	public static Boolean estaDentroDeRelacion(Date fecha, Date fechaDesde, Date fechaHasta) {
		if(esMayorIgual(fecha, fechaDesde) && (fechaHasta == null || esMenor(fecha, fechaHasta))){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}
