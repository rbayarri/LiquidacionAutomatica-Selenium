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

  private static final String host = "smtp.uncu.edu.ar";
  private static final String user = "rbayarri";//change accordingly  
  private static final String password = "contador4uncuyo";//change accordingly  

  private static final String toCBU = "jdorcemaine@uncu.edu.ar";//change accordingly
  private static final String toCBU2 = "afernandez@uncu.edu.ar";
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
  public static void sendMessage(String subject, String text, boolean intern, String type) {
    try {
      MimeMessage message = new MimeMessage(SendEmailTLS.session);
      message.setFrom(new InternetAddress(user + "@uncu.edu.ar"));
      if (type.equals("Contratos")) {
        if (intern) {
          message.addRecipient(Message.RecipientType.TO, new InternetAddress(toCBU));
          message.addRecipient(Message.RecipientType.CC, new InternetAddress(toCBU2));
        } else {
          message.addRecipient(Message.RecipientType.TO, new InternetAddress(toContratos));
          message.addRecipient(Message.RecipientType.CC, new InternetAddress("msalinas@uncu.edu.ar"));
          message.addRecipient(Message.RecipientType.CC, new InternetAddress("rfernandez@uncu.edu.ar"));
        }
      } else {
        // TODO: Verificar quién estará a cargo de incentivos
        message.addRecipient(Message.RecipientType.TO, new InternetAddress("ypinol@uncu.edu.ar"));
      }

      message.setSubject(subject);
      message.setText(text + "\n\nNo responder a esta casilla de correo. Este es un mail automático.\nResponder a msalinas@uncu.edu.ar");

      //send the message  
      Transport.send(message);

      System.out.println("message sent successfully...");

    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

  public static void sendMessageCBU(Group group, User user) {
    String message = "Buen día, no se encuentra en la nómina del Banco Patagonia los siguientes contratados: ";
    for (Liquidacion li : group.getLiquidacionesLiquidadas()) {
      if (li.getResultAutorizacion() != null) {
        LiquidacionContrato l = (LiquidacionContrato) li;

        message += "\nContratado: " + l.getBeneficiary();
        message += "\nOrden de pago: " + l.getOp().toString();
        message += "\nCUIT: " + l.getCUIT();
        message += "\nCantidad de comprobantes: " + l.getInvoices().size();
        for (Invoice invoice : l.getInvoices()) {
          message += "\nTipo: " + invoice.getType();
          message += "\nNúmero: " + invoice.getNumber();
          message += "\nFecha: " + invoice.getDate();
        }
        message += "\n";
      }
    }
    message += "\nPor tanto se excluyen de la liquidación " + group.getGroupName()
        + ", corresponde solicitar cuentas y anular las ordenes de pago correspondientes."
        + "La liquidación fue procesada por " + user.getPilagaUser();
    sendMessage("Liquidaciones excluidas por no tener CBU - " + group.getGroupName(), message, true, "Contratos");
  }
}
