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
			nick = usuario.getNombre().toLowerCase();
		}else{
			nick = usuario.getNombre().toLowerCase().substring(0, 1).concat(usuario.getApellido().toLowerCase());
		}
		return nick;
	}
}
