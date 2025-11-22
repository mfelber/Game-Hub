package gamehub.game_Hub.Module.Flags;

import lombok.Getter;

@Getter
public enum FriendRequestOption {

  EVERY_ONE("Every one"),
  NO_ONE("No one");

  private final String name;

  FriendRequestOption(final String name) {
    this.name = name;
  }

}
