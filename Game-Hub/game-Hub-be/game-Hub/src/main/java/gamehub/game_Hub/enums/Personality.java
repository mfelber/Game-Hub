package gamehub.game_Hub.enums;

import lombok.Getter;

@Getter
public enum Personality {

  INTROVERT("Introvert", "I'm quiet and reserved, and I prefer calm sessions with fewer people."),
  AMBIVERT("Ambivert", "I'm a mix of both: talkative with people I know, quiet with new ones."),
  EXTROVERT("Extrovert", "I'm outgoing and talkative, and I enjoy chatting while I play.");

  private final String label;
  private final String description;

  Personality(String label, String description) {
    this.label = label;
    this.description = description;
  }

}
