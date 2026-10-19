package gamehub.game_Hub.enums.Lfg;

import gamehub.game_Hub.enums.LfgOption;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Personality implements LfgOption {

  INTROVERT("Introvert", "I'm quiet and reserved, and I prefer calm sessions with fewer people."),
  AMBIVERT("Ambivert", "I'm a mix of both: talkative with people I know, quiet with new ones."),
  EXTROVERT("Extrovert", "I'm outgoing and talkative, and I enjoy chatting while I play.");

  private final String label;
  private final String description;

}
