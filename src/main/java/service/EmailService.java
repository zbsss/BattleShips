package service;

import model.dao.PlayerStatisticDAO;
import model.data.PlayerInfo;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Session;
import javax.mail.Transport;


public class EmailService {
    private final PlayerStatisticDAO ranking = new PlayerStatisticDAO();
    private final String sender = "bob.battleship@gmail.com";
    private final String host = "smtp.gmail.com";
    private final String nothing = "g425g356hwdhsfduy456u534dsgh654ujhfgd";

    public void rankingNotification(PlayerInfo toNotify){
        String subject = "You fell in the ranking!";

        String contents =
                "Hey " + toNotify.getName() +
                ", someone passed you in the ranking!<br>" +
                "You are now ranked " + ranking.getPlayerPlace(toNotify) +
                "<br><br>Good luck,<br>Bob";

        sendEmail(toNotify.getEmail(), subject, contents);
    }

    public void registerNotification(PlayerInfo player){
        String subject = "Battleships Registration";
        String contents =
                "Hey " + player.getName() +
                ", thank you for signing up for our game!<br><br>" +
                "Good luck on the sea,<br>Bob";
        sendEmail(player.getEmail(), subject, contents);
    }


    public void sendEmail(String recipient, String subject, String contents){
        System.out.println("SENDING EMAIL NOTIFICATION");
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(sender, nothing);
                    }
                });
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(sender));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(contents, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("EMAIL NOTIFICATION SENT TO: " + recipient);
        } catch (MessagingException e) {
            System.out.println("EMAIL NOTIFICATION FAILED");
            e.printStackTrace();
        }
    }
}
