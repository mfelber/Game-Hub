package gamehub.game_Hub.Email;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

import java.util.HashMap;
import java.util.Map;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender mailSender;
  private final SpringTemplateEngine templateEngine;

  @Async
  public void sendWelcomeEmail(final String to,
      final String username,
      final EmailTemplate emailTemplate,
      final String loginUrl,
      final String subject) throws MessagingException {
    String templateName;
    if (emailTemplate == null) {
      templateName = "welcome-email";
    } else {
      templateName = emailTemplate.getName();
    }

    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
        MULTIPART_MODE_MIXED,
        UTF_8.name());
    Map<String, Object> properties = new HashMap<>();
    properties.put("username", username);
    properties.put("loginUrl", loginUrl);

    Context context = new Context();
    context.setVariables(properties);

    helper.setFrom("contact@gamehub.com");
    helper.setTo(to);
    helper.setSubject(subject);

    String template = templateEngine.process(templateName, context);

    helper.setText(template, true);

    mailSender.send(mimeMessage);
  }

  public void sendResetPasswordEmail(final String to,
      final String username,
      final EmailTemplate emailTemplate,
      final String resetPasswordUrl,
      final String subject) throws MessagingException {
    String templateName;
    if (emailTemplate == null) {
      templateName = "reset-password-mail";
    } else {
      templateName = emailTemplate.getName();
    }

    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
        MULTIPART_MODE_MIXED,
        UTF_8.name());
    Map<String, Object> properties = new HashMap<>();
    properties.put("username", username);
    properties.put("resetPasswordUrl", resetPasswordUrl);

    Context context = new Context();
    context.setVariables(properties);

    helper.setFrom("contact@gamehub.com");
    helper.setTo(to);
    helper.setSubject(subject);

    String template = templateEngine.process(templateName, context);

    helper.setText(template, true);

    mailSender.send(mimeMessage);
  }

}
