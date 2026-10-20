package gamehub.game_Hub.enums.user;

import lombok.Getter;

@Getter
public enum PlayTogetherInvitesOption {

  FRIENDS("Friends"),
  NO_ONE("No one");

  private final String name;

  PlayTogetherInvitesOption(final String name) {
    this.name = name;
  }

}
