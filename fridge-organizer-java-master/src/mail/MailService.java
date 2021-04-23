package mail;
import java.util.Properties;
import javax.activation.*;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailService{
	static String subject,content;
	public static void sendMail(String recepient,String sub, String cont)  {
		subject = sub;
		content = cont;
		System.out.print("starting to send");
		Properties properties=new Properties();
		properties.put("mail.smtp.auth","true");
		properties.put("mail.smtp.starttls.enable","true");
		properties.put("mail.smtp.host","smtp.gmail.com");
		properties.put("mail.smtp.port","587");
		properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		
//		String myMail="exchangeit001@gmail.com";
//		String password="Abishekganesh4";
		String myMail="servicesatredbug@gmail.com";
		String password="servicesredbug";
		
		Session session=Session.getInstance(properties,new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(myMail,password);
				
			}
		});
		Message message=prepareMessage(session,myMail,recepient);
		try {
			Transport.send(message);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print("Message sent successfully");
		
		
		
	}
	
	private static Message prepareMessage(Session session,String myMail,String recepient) {
		
		try {
			MimeMessage message=new MimeMessage(session);
			message.setFrom(new InternetAddress(myMail));
			message.setRecipient(Message.RecipientType.TO,new InternetAddress(recepient) );
			message.setSubject(subject);
			message.setText(content);
			return message;
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args)  {
//		MailService.sendMail("18eucs008@skcet.ac.in");
	}
	
}