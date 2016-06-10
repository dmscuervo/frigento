package com.soutech.frigento.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.soutech.frigento.dto.Parametros;
import com.soutech.frigento.model.Estado;
import com.soutech.frigento.model.Pedido;
import com.soutech.frigento.model.Usuario;
import com.soutech.frigento.model.Venta;
import com.sun.xml.ws.util.ByteArrayDataSource;

@Component
public class SendMailSSL {
	
	Logger logger = Logger.getLogger(this.getClass());
//	@Context 
//	protected SecurityContext sc;
	
	public static void main(String args[]) {
		try {
			Pedido pedido = new Pedido();
			pedido.setId(1);
			Estado estado = new Estado();
			estado.setId(new Short(Constantes.ESTADO_PEDIDO_ANULADO));
			pedido.setEstado(estado);
			pedido.setVersion((short)2);
			
			new SendMailSSL().enviarCorreoPedido(pedido, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void enviarCorreoPedido(Pedido pedido, ByteArrayOutputStream attachment, String attachFileName) throws Exception{
		String subject = "Pedido N° " + Utils.generarNroRemito(pedido);
		
		StringBuilder bodyText = new StringBuilder();
		InputStream is = null;
		
		if(pedido.getEstado().getId().equals(new Short(Constantes.ESTADO_PEDIDO_CONFIRMADO))){
			
			if(pedido.getVersion().shortValue() > 1){
				is = this.getClass().getClassLoader().getResourceAsStream("mail/body_pedido_modificado.html");
				subject = subject.concat(" - MODIFICADO");
			}else{
				is = this.getClass().getClassLoader().getResourceAsStream("mail/body_pedido_confirmado.html");
			}
			
		}else if(pedido.getEstado().getId().equals(new Short(Constantes.ESTADO_PEDIDO_ENTREGADO))){
			is = this.getClass().getClassLoader().getResourceAsStream("mail/body_pedido_entregado.html");
			subject = subject.concat(" - ENTREGADO");
		}else if(pedido.getEstado().getId().equals(new Short(Constantes.ESTADO_PEDIDO_ANULADO))){
			is = this.getClass().getClassLoader().getResourceAsStream("mail/body_pedido_anulado.html");
			subject = subject.concat(" - ANULADO");
		}
		try {
			BufferedReader fr = new BufferedReader(new InputStreamReader(is));
			String linea = fr.readLine();
			while(linea != null){
				bodyText.append(linea);
				linea = fr.readLine();
			}
			fr.close();
		} catch (FileNotFoundException e) {
			logger.error("No se pudo generar el body del mail. Archivo html inexistente.");
			throw new Exception(e);
		} catch (IOException e) {
			logger.error("No se pudo generar el body del mail. Error al leer archivo html.");
			throw new Exception(e);
		}
		
		enviarMail(subject, bodyText.toString(), attachment, attachFileName, Parametros.getValor(Parametros.SMTP_GMAIL_DESTINATARIOS_PEDIDOS), Parametros.getValor(Parametros.SMTP_GMAIL_DESTINATARIOS_CC_PEDIDOS));
	}

	public void enviarCorreoVenta(Venta venta, ByteArrayOutputStream attachment, String attachFileName) throws Exception{
		String subject = "Pedido N° " + Utils.generarNroRemito(venta);
		
		StringBuilder bodyText = new StringBuilder();
		InputStream is = null;
		
		if(venta.getEstado().getId().equals(new Short(Constantes.ESTADO_PEDIDO_CONFIRMADO))){
			
			if(venta.getVersion().shortValue() > 1){
				is = this.getClass().getClassLoader().getResourceAsStream("mail/body_venta_modificado.html");
				subject = subject.concat(" - MODIFICADO");
			}else{
				is = this.getClass().getClassLoader().getResourceAsStream("mail/body_venta_confirmado.html");
			}
			
		}else if(venta.getEstado().getId().equals(new Short(Constantes.ESTADO_PEDIDO_ENTREGADO))){
			is = this.getClass().getClassLoader().getResourceAsStream("mail/body_venta_entregado.html");
			subject = subject.concat(" - ENTREGADO");
		}else if(venta.getEstado().getId().equals(new Short(Constantes.ESTADO_PEDIDO_ANULADO))){
			is = this.getClass().getClassLoader().getResourceAsStream("mail/body_venta_anulado.html");
			subject = subject.concat(" - ANULADO");
		}
		try {
			BufferedReader fr = new BufferedReader(new InputStreamReader(is));
			String linea = fr.readLine();
			while(linea != null){
				bodyText.append(linea);
				linea = fr.readLine();
			}
			fr.close();
		} catch (FileNotFoundException e) {
			logger.error("No se pudo generar el body del mail. Archivo html inexistente.");
			throw new Exception(e);
		} catch (IOException e) {
			logger.error("No se pudo generar el body del mail. Error al leer archivo html.");
			throw new Exception(e);
		}
		
		enviarMail(subject, bodyText.toString(), attachment, attachFileName, venta.getUsuario().getEmail(), Parametros.getValor(Parametros.SMTP_GMAIL_DESTINATARIOS_CC_VENTAS));
	}
	
	private void enviarMail(String subject, String bodyText, ByteArrayOutputStream attachment, String attachFileName, String to, String cc) throws Exception {
		
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", Parametros.getValor(Parametros.SMTP_GMAIL_PORT));
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", Parametros.getValor(Parametros.SMTP_GMAIL_PORT));

		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(Parametros.getValor(Parametros.SMTP_GMAIL_REMITENTE), Parametros.getValor(Parametros.SMTP_GMAIL_PASSWORD));
				}
			});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(Parametros.getValor(Parametros.SMTP_GMAIL_REMITENTE)));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));
			if(cc != null
					&& !cc.equals("")){
				message.setRecipients(Message.RecipientType.CC,
						InternetAddress.parse(cc));
			}
			message.setSubject(subject);
			
			BodyPart texto = new MimeBodyPart();
			texto.setContent(bodyText, "text/html");
			
			MimeMultipart multiParte = new MimeMultipart();
			multiParte.addBodyPart(texto);
			
			if(attachment != null){
				BodyPart adjunto = new MimeBodyPart();
				adjunto.setDataHandler(new DataHandler(new ByteArrayDataSource(attachment.toByteArray(), "application/pdf")));
				adjunto.setFileName(attachFileName);
				multiParte.addBodyPart(adjunto);
			}
			
			message.setContent(multiParte);
			message.saveChanges();
			Transport.send(message);

		} catch (MessagingException e) {
			throw new Exception(e);
		}
	}

	public void enviarCorreoRegistracion(Usuario usuario, String protocolo, String host, String port, String contex) throws Exception {
		String subject = "Registración Frigento";
		StringBuilder bodyText = new StringBuilder();
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("mail/body_registracion.html");
		try {
			BufferedReader fr = new BufferedReader(new InputStreamReader(is));
			String linea = fr.readLine();
			//usuario/dmscuervo/asdlkjaklsdj?confirmarReg
			while(linea != null){
				if(linea.contains("{protocolo}")){
					linea = linea.replace("{protocolo}", protocolo);
				}
				if(linea.contains("{host}")){
					linea = linea.replace("{host}", host);
				}
				if(linea.contains("{port}")){
					linea = linea.replace("{port}", port);
				}
				if(linea.contains("{context}")){
					linea = linea.replace("{context}", contex);
				}
				if(linea.contains("{username}")){
					linea = linea.replace("{username}", usuario.getUsername());
				}
				if(linea.contains("{valid}")){
					linea = linea.replace("{valid}", Encriptador.encriptarPassword(usuario.getId().toString()));
				}
				bodyText.append(linea);
				linea = fr.readLine();
			}
			fr.close();
		} catch (FileNotFoundException e) {
			logger.error("No se pudo generar el body del mail. Archivo html inexistente.");
			throw new Exception(e);
		} catch (IOException e) {
			logger.error("No se pudo generar el body del mail. Error al leer archivo html.");
			throw new Exception(e);
		}
		enviarMail(subject, bodyText.toString(), null, null, usuario.getEmail(), null);
	}

	public void enviarCorreoProblema(String mensaje, Exception ex) {
		String subject = "Problema en el sistema";
		StringBuilder bodyText = new StringBuilder();
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("mail/body_error.html");
		try {
			BufferedReader fr = new BufferedReader(new InputStreamReader(is));
			String linea = fr.readLine();
			while(linea != null){
				if(linea.contains("{detalle}")){
					linea = linea.replace("{detalle}", mensaje);
				}
				if(linea.contains("{stack}")){
					linea = linea.replace("{stack}", PrinterStack.getStackTraceAsString(ex));
				}
				bodyText.append(linea);
				linea = fr.readLine();
			}
			fr.close();
			enviarMail(subject, bodyText.toString(), null, null, Parametros.SMTP_GMAIL_REMITENTE, null);
		} catch (FileNotFoundException e) {
			logger.info("No se pudo enviar correo de error en aplicación.");
			logger.error(PrinterStack.getStackTraceAsString(e));
		} catch (IOException e) {
			logger.info("No se pudo enviar correo de error en aplicación.");
			logger.error(PrinterStack.getStackTraceAsString(e));
		} catch (Exception e) {
			logger.info("No se pudo enviar correo de error en aplicación.");
			logger.error(PrinterStack.getStackTraceAsString(e));
		}
	}
	
}
