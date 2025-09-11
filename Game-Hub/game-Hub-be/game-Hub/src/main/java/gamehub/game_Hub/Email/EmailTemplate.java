package gamehub.game_Hub.Email;

import lombok.Getter;

@Getter

public enum EmailTemplate {

  WELCOME_EMAIL("welcome-email");

  private final String name;

  EmailTemplate(String name) {
    this.name = name;
  }

}
