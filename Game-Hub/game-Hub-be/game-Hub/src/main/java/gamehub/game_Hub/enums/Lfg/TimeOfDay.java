package gamehub.game_Hub.enums.Lfg;

import gamehub.game_Hub.enums.IconLfgOption;
import gamehub.game_Hub.enums.LfgOption;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TimeOfDay implements IconLfgOption {

  MORNING("Morning", "6 – 12", "fa-solid fa-mug-hot"),
  AFTERNOON("Afternoon", "12 – 18", "fa-solid fa-sun"),
  EVENING("Evening", "18 – 23", "fa-solid fa-cloud-moon"),
  NIGHT("Night", "23 – 6", "fa-solid fa-moon");

  private final String label;
  private final String description;
  private final String icon;

}
