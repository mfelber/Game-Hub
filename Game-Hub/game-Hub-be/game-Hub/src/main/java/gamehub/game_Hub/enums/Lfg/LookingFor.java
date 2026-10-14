package gamehub.game_Hub.enums.Lfg;

import gamehub.game_Hub.enums.IconLfgOption;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LookingFor implements IconLfgOption {

  FRIEND("Friend", "Someone to play with regularly and stay in touch.", "fa-solid fa-user"),
  DUO("Duo Partner", "One teammate to queue or co-op with.","fa-solid fa-user-group"),
  SQUAD("Squad", "A group of players for team games.", "fa-solid fa-users");

  private final String label;
  private final String description;
  private final String icon;
}
