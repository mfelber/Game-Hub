package gamehub.game_Hub.enums.Lfg;

import gamehub.game_Hub.enums.LfgOption;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Commitment implements LfgOption {

  LONG_TERM("Long-term", "I'm looking for someone to play with regularly over weeks or months."),
  SHORT_TERM("Short-term", "I'm looking for a teammate for a few days, for example to finish a quest or event."),
  ONE_SESSION("One session", "I'm looking for someone to play with for just one gaming session.");

  private final String label;
  private final String description;

}
