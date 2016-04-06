package com.soutech.frigento.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class PrinterStack {

	/** 
	* Gets the exception stack trace as a string. 
	* @param exception 
	* @return 
	*/
	public static String getStackTraceAsString(Exception exception) 
	{ 
	StringWriter sw = new StringWriter(); 
	PrintWriter pw = new PrintWriter(sw); 
	pw.print(" [ "); 
	pw.print(exception.getClass().getName()); 
	pw.print(" ] "); 
	pw.print(exception.getMessage()); 
	exception.printStackTrace(pw); 
	return sw.toString(); 
	}
	
	public static String getThrowableStackTraceAsString(Throwable exception) 
	{ 
	StringWriter sw = new StringWriter(); 
	PrintWriter pw = new PrintWriter(sw); 
	pw.print(" [ "); 
	pw.print(exception.getClass().getName()); 
	pw.print(" ] "); 
	pw.print(exception.getMessage()); 
	exception.printStackTrace(pw); 
	return sw.toString(); 
	}
}
