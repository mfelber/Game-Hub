package gamehub.game_Hub.Module.userSettings;

import lombok.Getter;

@Getter
public enum Access {

  EVERYONE("Everyone"),
  FRIENDS("Friends"),
  NO_ONE("No one");

  private final String accessName;

  Access(final String accessName) {
    this.accessName = accessName;
  }
}
