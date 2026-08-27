package gamehub.game_Hub.Email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import gamehub.game_Hub.Module.User.User;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminEmailService {

  private final EmailService emailService;

  @Value("${application.mailing.frontend.login-url}")
  private String logInUrl;

  @Async
  public void sendBannedUserEmail(final User user, final String customMsg, String banReason, String description) {
    try {
      // TODO dont send user.getId() but send id of ban when implementing chat between user and admin
      // on fe show report id with # report.getId()
      String appealUrl = "http://localhost:4200/send-appeal?appeal=" + user.getId();
      emailService.sendBannedUserEmail(user.getEmail(), user.getName(), banReason, customMsg, description,
          EmailTemplate.USER_BANNED_EMAIL, appealUrl, "Your GameHub account has been banned — Appeal available");
    } catch (MessagingException e) {
      System.out.println(e);
    }
  }

  @Async
  public void sendAccountRestoredEmail(final User user) {
    try {
      emailService.sendAccountRestored(user.getEmail(), user.getName(), EmailTemplate.USER_ACCOUNT_RESTORED_EMAIL,
          logInUrl, "Your GameHub account has been successfully restored");
    } catch (MessagingException e) {
      System.out.println(e);
    }
  }

  @Async
  public void sendSuspendedAccountEmail(final User user, final String violatedGuideline, final String customMsg,
      final String suspensionEndDate) {
    try {
      emailService.sendSuspendedAccountEmail(user.getEmail(), user.getName(), violatedGuideline, customMsg,
          suspensionEndDate, EmailTemplate.USER_SUSPENDED_EMAIL, "Your GameHub has been suspended");
    } catch (MessagingException e) {
      System.out.println(e);
    }
  }

}
