package gamehub.game_Hub.enums.Lfg;

import gamehub.game_Hub.enums.LfgOption;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TimeOfDay implements LfgOption {

  MORNING("Morning", "6 – 12"),
  AFTERNOON("Afternoon", "12 – 18"),
  EVENING("Evening", "18 – 23"),
  NIGHT("Night", "23 – 6");

  private final String label;
  private final String description;

}
