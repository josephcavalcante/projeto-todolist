package comunicacao;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.activation.*;

public class Mensageiro {
    public static void enviarEmail(String destinatario, String mensagem) {
        final String remetente = "projetopoo00@gmail.com";
        final String senha = "rmlk fgde guzf pzpc";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remetente, senha);
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(remetente));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            msg.setSubject("Relatório de Tarefas do Dia");
            
            BodyPart corpo = new MimeBodyPart();
            corpo.setText(mensagem);

            MimeBodyPart anexo = new MimeBodyPart();
            DataSource source = new FileDataSource("relatorio.pdf");
            anexo.setDataHandler(new DataHandler(source));
            anexo.setFileName("relatorio.pdf");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(corpo);
            multipart.addBodyPart(anexo);

            msg.setContent(multipart);

            Transport.send(msg);
            System.out.println("E-mail enviado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Falha ao enviar o e-mail.");
        }
    }
} 