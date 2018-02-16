package fyodor.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

@Component
public class EmailConfirm {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessageSource messageSource;

    private Properties smtpProperties;
    private Session session;
    private Message message;
    private String emailAddress;
    private String emailPassword;
    private String hash;
    private Locale locale;

    public void sendMail(String appUrl, Locale locale, String username, String email, String hash) {
        this.hash = hash;
        this.locale = locale;
        loadEmailProperty();
        setSmtpProperties();
        authenticateSender();
        send(appUrl, username, email);
    }

    public void loadEmailProperty() {
        InputStream fis;
        Properties emailProperty = new Properties();
        try {
            fis = this.getClass().getClassLoader().getResourceAsStream("email.properties");
            emailProperty.load(fis);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        emailAddress = emailProperty.getProperty("email");
        emailPassword = emailProperty.getProperty("password");
    }

    public void setSmtpProperties() {
        smtpProperties = new Properties();
        smtpProperties.put("mail.smtp.auth", "true");
        smtpProperties.put("mail.smtp.starttls.enable", "true");
        smtpProperties.put("mail.smtp.host", "smtp.gmail.com");
        smtpProperties.put("mail.smtp.port", "587");
    }

    public void authenticateSender() {
        session = Session.getInstance(smtpProperties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAddress, emailPassword);
            }
        });
    }

    public void send(String appUrl, String username, String email) {
        try {
            message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailAddress));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            String subject = messageSource.getMessage("registration.letter.subject", null, locale);
            String messageGreetings = messageSource.getMessage("registration.letter.greetings", new String[]{username}, locale);
            String messageBody = messageSource.getMessage("registration.letter.clickLink", null, locale);
            message.setSubject(subject);
            message.setText(messageGreetings + "\n\n" + messageBody + "\n" + appUrl + "/registrationconfirm?username=" + username + "&hash=" + hash);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
