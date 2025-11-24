package gamehub.game_Hub.Module.Flags;

import lombok.Getter;

@Getter
public enum GroupInvitesOption {

  FRIENDS("Friends"),
  EVERY_ONE("Every one"),
  NO_ONE("No one");

  private final String name;

  GroupInvitesOption(final String name) {
    this.name = name;
  }

}
