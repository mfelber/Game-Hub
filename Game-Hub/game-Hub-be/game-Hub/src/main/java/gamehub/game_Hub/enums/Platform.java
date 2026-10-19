package gamehub.game_Hub.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Platform {

  PC("PC","fa-solid fa-desktop"),
  PS("PlayStation","fa-brands fa-playstation"),
  XBOX("Xbox","fa-brands fa-xbox"),
  NINTENDO("Nintendo","fa-solid fa-gamepad"),
  MOBILE("Mobile","fa-solid fa-mobile-screen");

  private final String platformName;
  private final String platformIcon;
}
