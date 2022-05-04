/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package liquidacionautomatica.entities;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

public class SendEmailTLS {

  private static final String host = "smtp.hostinger.com.ar";
  private static final String user = "contacto@mabay.com.ar";//change accordingly  
  private static final String password = ":]mu6wK8gH6";//change accordingly  

  private static final String toCBU = "lcalanoce@uncu.edu.ar";//change accordingly  
  private static final String toContratos = "contratos@uncu.edu.ar";//change accordingly  

//Get the session object  
  private static final Properties props = new Properties();

  private static final Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
    protected PasswordAuthentication getPasswordAuthentication() {
      return new PasswordAuthentication(user, password);
    }
  });

  static {
    props.put("mail.smtp.host", host);
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.socketFactory.port", "587");
    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    props.put("mail.smtp.port", "587");
  }

  //Compose the message  
  public static void sendMessage(String subject, String text, boolean intern) {
    try {
      MimeMessage message = new MimeMessage(SendEmailTLS.session);
      message.setFrom(new InternetAddress(user));
      if (intern) {
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toCBU));
      } else {
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toContratos));
      }
      message.setSubject(subject);
      message.setText(text);

      //send the message  
      Transport.send(message);

      System.out.println("message sent successfully...");

    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

  public static void sendMessageCBU(Group group) {
    String message = "";
    for (Liquidacion li : group.getLiquidacionesLiquidadas()) {
      if (li.getResultAutorizacion() != null) {
        LiquidacionContrato l = (LiquidacionContrato) li;
        message += l.getOp().toString();
        message += " - ";
        message += l.getBeneficiary();
        message += " - ";
        message += l.getCUIT();
        message += "\n";
      }
    }
    sendMessage("Liquidaciones excluidas por no tener CBU - " + group.getGroupName(), message, true);
  }
}