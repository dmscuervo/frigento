package com.soutech.frigento.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author dsouza
 *
 */
public class Encriptador implements PasswordEncoder{
	
	// algoritmos
	public static String MD2 = "MD2";
	public static String MD5 = "MD5";
	public static String SHA1 = "SHA-1";
	public static String SHA256 = "SHA-256";
	public static String SHA384 = "SHA-384";
	public static String SHA512 = "SHA-512";

	/***
	 * Convierte un arreglo de bytes a String usando valores hexadecimales
	 * 
	 * @param digest arreglo de bytes a convertir
	 * @return String creado a partir de <code>digest</code>
	 */
	private static String toHexadecimal(byte[] digest) {
		String hash = "";
		for (byte aux : digest) {
			int b = aux & 0xff;
			if (Integer.toHexString(b).length() == 1)
				hash += "0";
			hash += Integer.toHexString(b);
		}
		return hash;
	}
	
	@SuppressWarnings("unused")
	private static String toDecimal(byte[] digest) {
		String hash = "";
		for (byte aux : digest) {
			int b = aux & 0xff;
			if (Integer.toHexString(b).length() == 1)
				hash += "0";
			hash += b;
		}
		return hash;
	}

	/***
	 * Encripta un mensaje de texto mediante algoritmo de resumen de mensaje.
	 * 
	 * @param message texto a encriptar
	 * @param algorithm algoritmo de encriptacion, puede ser: MD2, MD5, SHA-1,
	 *            SHA-256, SHA-384, SHA-512
	 * @return mensaje encriptado
	 */
	public static String getStringMessageDigest(String message, String algorithm) {
		byte[] digest = null;
		byte[] buffer = message.getBytes();
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			messageDigest.reset();
			messageDigest.update(buffer);
			digest = messageDigest.digest();
		} catch (NoSuchAlgorithmException ex) {
			System.out.println("Error creando Digest");
		}
		return toHexadecimal(digest);
	}

	public static String encriptarPassword(String password) {
		String md5 = getStringMessageDigest(password, MD5);
		return getStringMessageDigest(md5, SHA256);
	}
	
	public static void main(String[] args) {
		String encriptarPassword = Encriptador.encriptarPassword("");
		System.out.println(encriptarPassword);
	}

	@Override
	public String encode(CharSequence arg0) {
		return encriptarPassword(arg0.toString());
	}

	@Override
	public boolean matches(CharSequence arg0, String arg1) {
		if(encriptarPassword(arg0.toString()).equals(arg1)){
			return true;
		}
		return false;
	}
}