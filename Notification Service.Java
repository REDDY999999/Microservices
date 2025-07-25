
package com.onboarding.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

@SpringBootApplication
public class NotificationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
}

@RestController
@RequestMapping("/notify")
class NotificationController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/onboarding-confirmation")
    public ResponseEntity<String> sendOnboardingConfirmation(@RequestBody NotificationRequest request) {
        try {
            emailService.sendEmail(request.getEmail(), "Onboarding Confirmation",
                    "Dear " + request.getName() + ",\n\nWelcome to the company! Your onboarding is now confirmed.\n\nRegards,\nHR Team");
            return ResponseEntity.ok("Email sent successfully.");
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to send email.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

class NotificationRequest {
    private String email;
    private String name;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

@Service
class EmailService {

    private final String from = "noreply@onboarding.com";
    private final String host = "smtp.mailtrap.io"; // Replace with your SMTP host
    private final String username = "username"; // Replace with SMTP username
    private final String password = "password"; // Replace with SMTP password

    public void sendEmail(String to, String subject, String body) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }
}
