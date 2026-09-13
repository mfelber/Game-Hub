package gamehub.game_Hub.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
  ONLINE,
  PLAYING,
  OFFLINE,
  AWAY;

  @JsonValue
  public String getValue() {
    String value = name();
    return value.substring(0,1) + value.substring(1).toLowerCase();
  }
}
