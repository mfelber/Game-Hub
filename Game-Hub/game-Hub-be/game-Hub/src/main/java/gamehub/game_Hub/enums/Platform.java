package gamehub.game_Hub.enums;

import lombok.Getter;

@Getter
public enum Platform {

  PC("PC","fa-solid fa-desktop"),
  NINTENDO("Nintendo","fa-solid fa-gamepad"),
  XBOX("Xbox","fa-brands fa-xbox"),
  PS("PlayStation","fa-brands fa-playstation"),
  MOBILE("Mobile","fa-solid fa-mobile-screen");

  private final String platformName;
  private final String platformIcon;

  Platform(final String platformName, final String platformIcon) {
    this.platformName = platformName;
    this.platformIcon = platformIcon;
  }
}
