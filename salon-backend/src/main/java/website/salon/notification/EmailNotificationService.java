package website.salon.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailNotificationService {

    private static final Logger log = LoggerFactory.getLogger(EmailNotificationService.class);

    private final JavaMailSender mailSender;
    private final boolean enabled;
    private final String fromAddress;

    public EmailNotificationService(Optional<JavaMailSender> mailSender,
                                     @Value("${app.notifications.email.enabled:false}") boolean enabled,
                                     @Value("${app.notifications.email.from:}") String fromAddress) {
        this.mailSender = mailSender.orElse(null);
        this.enabled = enabled;
        this.fromAddress = fromAddress;
    }

    public void sendPlainTextEmail(String to, String subject, String body) {
        if (!enabled) {
            return;
        }
        if (mailSender == null) {
            log.warn("Email notifications are enabled but no JavaMailSender is configured (check spring.mail.host); skipping email to {}", to);
            return;
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            if (!fromAddress.isBlank()) {
                message.setFrom(fromAddress);
            }
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (Exception ex) {
            log.error("Failed to send email to {}: {}", to, ex.getMessage(), ex);
        }
    }
}
