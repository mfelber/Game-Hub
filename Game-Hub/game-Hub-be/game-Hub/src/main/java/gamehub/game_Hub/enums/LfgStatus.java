package gamehub.game_Hub.enums;

import lombok.Getter;

@Getter
public enum LfgStatus {

  ACTIVE("Active"),
  CLOSED("Closed"),
  EXPIRED("Expired");

  private final String label;

  LfgStatus(String label) {
    this.label = label;
  }

}
