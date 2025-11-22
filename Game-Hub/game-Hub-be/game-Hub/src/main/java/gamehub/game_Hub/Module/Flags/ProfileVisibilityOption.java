package gamehub.game_Hub.Module.Flags;

import lombok.Getter;

@Getter
public enum ProfileVisibilityOption {

  FRIENDS("Friends"),
  EVERY_ONE("Every one"),
  NO_ONE("No one");

  private final String name;

  ProfileVisibilityOption(final String name) {
    this.name = name;
  }

}
