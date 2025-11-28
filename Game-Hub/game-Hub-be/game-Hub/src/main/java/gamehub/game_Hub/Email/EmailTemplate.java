package gamehub.game_Hub.Email;

import lombok.Getter;

@Getter

public enum EmailTemplate {

  WELCOME_EMAIL_ADULT("welcome-email-adult"),
  WELCOME_EMAIL_CHILD("welcome-email-child"),
  RESET_PASSWORD_MAIL("reset-password-mail");

  public final String name;

  EmailTemplate(String name) {
    this.name = name;
  }

}
