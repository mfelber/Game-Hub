package gamehub.game_Hub.enums.Lfg;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LfgStatus {

  ACTIVE("Active"),
  CLOSED("Closed"),
  EXPIRED("Expired");

  private final String label;

}
