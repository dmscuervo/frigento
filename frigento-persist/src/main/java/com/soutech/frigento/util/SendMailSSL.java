package com.soutech.frigento.util;

import java.io.ByteArrayOutputStream;
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

import org.springframework.stereotype.Component;

import com.soutech.frigento.model.Parametro;
import com.soutech.frigento.model.Pedido;
import com.sun.xml.internal.ws.util.ByteArrayDataSource;

@Component
public class SendMailSSL {
	
	public static void main(String args[]) {
		new SendMailSSL().enviarMail("Asunto", "Cuerpo del mensaje", null, null);
	}
	
	public void enviarCorreoPedido(Pedido pedido, ByteArrayOutputStream attachment, String attachFileName){
		String subject = "Pedido N° " + Utils.generarNroRemito(pedido);
		StringBuilder bodyText = new StringBuilder("Hola Fede,<br/><br/>");
		bodyText.append("Te adjunto ");
		if(pedido.getVersion().intValue() > 1){
			bodyText.append("una modificación sobre el pedido.");
		}else{
			bodyText.append("un nuevo pedido.");
		}
		bodyText.append("<br/><br/>");
		bodyText.append("Por favor si de lo pedido no tenes algo, avisame.");
		bodyText.append("<br/><br/>");
		bodyText.append("Gracias!!<br/>");
		bodyText.append("Loli");
		enviarMail(subject, bodyText.toString(), attachment, attachFileName);
	}
	
	
	private void enviarMail(String subject, String bodyText, ByteArrayOutputStream attachment, String attachFileName) {
		
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", Parametro.SMTP_GMAIL_PORT);
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", Parametro.SMTP_GMAIL_PORT);

		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(Parametro.SMTP_GMAIL_REMITENTE, Parametro.SMTP_GMAIL_PASSWORD);
				}
			});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(Parametro.SMTP_GMAIL_REMITENTE));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(Parametro.SMTP_GMAIL_DESTINATARIOS_CC));
			message.setSubject(subject);
			
			BodyPart texto = new MimeBodyPart();
			texto.setText(bodyText);
			
			MimeMultipart multiParte = new MimeMultipart();
			multiParte.addBodyPart(texto);
			
			if(attachment != null){
				BodyPart adjunto = new MimeBodyPart();
				adjunto.setDataHandler(new DataHandler(new ByteArrayDataSource(attachment.toByteArray(), "application/pdf")));
				adjunto.setFileName(attachFileName);
				multiParte.addBodyPart(adjunto);
			}
			
			message.setContent(multiParte);
			
			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
