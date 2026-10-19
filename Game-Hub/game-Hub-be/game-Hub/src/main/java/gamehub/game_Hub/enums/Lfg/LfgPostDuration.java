package gamehub.game_Hub.enums.Lfg;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LfgPostDuration {

  ONE_DAY("1 Day", 1, false),
  THREE_DAYS("3 Days", 3 , false),
  SEVEN_DAYS("7 Days", 7, false),
  FOURTEEN_DAYS("14 Days", 14, true),
  THIRTY_DAYS("30 Days", 30, true);

  private final String label;
  private final int days;
  private final boolean supporterOnly;
}
