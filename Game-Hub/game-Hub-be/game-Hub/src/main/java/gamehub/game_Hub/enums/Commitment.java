package gamehub.game_Hub.enums;

import lombok.Getter;

@Getter
public enum Commitment {

  LONG_TERM("Long-term", "I'm looking for someone to play with regularly over weeks or months."),
  SHORT_TERM("Short-term", "I need a teammate for a few days, for example to finish a quest or event."),
  ONE_SESSION("One session", "Just one game session");

  private final String label;
  private final String description;

  Commitment(String label, String description) {
    this.label = label;
    this.description = description;
  }

}
