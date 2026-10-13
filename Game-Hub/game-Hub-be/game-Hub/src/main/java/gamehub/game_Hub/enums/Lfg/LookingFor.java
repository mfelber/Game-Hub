package gamehub.game_Hub.enums.Lfg;

import lombok.Getter;

@Getter
public enum LookingFor {

  FRIEND("Friend"),
  DUO("Duo Partner"),
  SQUAD("Squad");

  private final String label;

  LookingFor(final String label) {
    this.label  = label;
  }
}
