package cw.gasbygas.util;

import cw.gasbygas.domain.UserStatus;
import cw.gasbygas.exceptionHandling.EmailException;
import cw.gasbygas.model.GasRequest;
import cw.gasbygas.model.Token;
import cw.gasbygas.model.User;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendVerificationEmail(String toEmail) {
        try {
            String content = "<html><body>"
                    + "<h3>Welcome to Gas System</h3>"
                    + "<p>Please verify your email by clicking the link below:</p>"
                    + "<a href='http://your-website.com/verify?email=" + toEmail + "'>Verify Email</a>"
                    + "</body></html>";

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Verify Your Gas System Account");
            helper.setText(content, true);  // Set to 'true' for HTML content

            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailException("Failed to send verification email", e);
        }
    }

    public void sendTokenEmail(String toEmail, Token token) {
        try {

            String content = "<html><body>"
                    + "<h3>Your Gas Pickup Token</h3>"
                    + "<p>Your token ID is: " + token.getId() + "</p>"
                    + "<p>Your pickup date is: " + token.getPickupDate() + "</p>"
                    + "</body></html>";

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Gas Pickup Details");
            helper.setText(content, true);  // Set to 'true' for HTML content

            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailException("Failed to send token email", e);
        }
    }

    public void sendReminderEmail(String toEmail, Token token) {
        try {
            String content = "<html><body>"
                    + "<h3>Reminder: Gas Pickup</h3>"
                    + "<p>Your pickup token ID is: " + token.getId() + "</p>"
                    + "<p>Pickup Date: " + token.getPickupDate() + "</p>"
                    + "</body></html>";

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Gas Pickup Reminder");
            helper.setText(content, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailException("Failed to send reminder email", e);
        }
    }

    public void sendWelcomeEmail(String toEmail, String name) {
        try {
            String content = "<html><body>"
                    + "<h3>Welcome to GasbyGas!</h3>"
                    + "<p>Dear  " + name + ",</p>"
                    + "<p>You can request gas after we verified your account. Thank you for the patient.</p>"
                    + "</body></html>";

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Welcome to GasbyGas!");
            helper.setText(content, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailException("Failed to send welcome email", e);
        }
    }

    public void sendAccountStatusEmail(String toEmail, User user) {
        try {
            String content = "<html><body>"
                    + "<p>Dear " + user.getName() + ",</p>"
                    + "<p>Your account is " + user.getStatus() + " now!</p>"
                    + "</body></html>";

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Account status update!");
            helper.setText(content, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailException("Failed to send account status email", e);
        }
    }

    public void sendDomesticCustomerRequestEmail(String toEmail, Token token, GasRequest request) {
        try {
            String content = "<html><body>"
                    + "<h3>Your Gas Pickup Token</h3>"
                    + "<p>Dear " + request.getUser().getName() + ",</p>"
                    + "<p>Your token ID is: " + token.getId() + "</p>"
                    + "<p>Your pickup date is: " + token.getPickupDate() + "</p>"
                    + "<p>Your outlet is: " + request.getOutlet() + "</p>"
                    + "<p>Your request is: " + request.getGas() + "</p>"
                    + "<p>Your total price is : Rs." + request.getTotalPrice() + "</p>"
                    + "</body></html>";

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Gas request Received");
            helper.setText(content, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailException("Failed to send domestic customer request email", e);
        }
    }

    public void sendDomesticCustomerRequestStatusUpdateEmail(String toEmail) {}
    public void sendDomesticCustomerPaymentRequestEmail(String toEmail) {}
    public void sendTokenReallocateEmail(String toEmail) {}
    public void sendEmailToCustomerDayBeforeDelivery(String toEmail) {}

    public void sendBusinessCustomerRequestEmail(String toEmail, GasRequest request) {
        try {
            String content = "<html><body>"
                    + "<h3>Your Gas Pickup Token</h3>"
                    + "<p>Dear " + request.getUser().getName() + ",</p>"
                    + "<p>Your outlet is: " + request.getOutlet() + "</p>"
                    + "<p>Your request is: " + request.getGas() + "</p>"
                    + "<p>Your request quantity is: " + request.getQuantity() + "</p>"
                    + "<p>Your unit price is: Rs." + request.getUnitPrice() + "</p>"
                    + "<p>Your total price is : Rs." + request.getTotalPrice() + "</p>"
                    + "</body></html>";

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Gas request Received");
            helper.setText(content, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailException("Failed to send domestic customer request email", e);
        }
    }

    public void sendBusinessCustomerRequestStatusUpdateEmail(String toEmail) {}

    public void sendOutletRequestEmail(String toEmail) {}

    public void sendOutletRequestStatusUpdateEmail(String toEmail) {}
}
