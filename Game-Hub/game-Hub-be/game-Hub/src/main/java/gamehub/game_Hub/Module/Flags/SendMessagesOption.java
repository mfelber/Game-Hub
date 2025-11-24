package gamehub.game_Hub.Module.Flags;

import lombok.Getter;

@Getter
public enum SendMessagesOption {

  FRIENDS("Friends"),
  NO_ONE("No one");

  private final String name;

  SendMessagesOption(final String name) {
    this.name = name;
  }

}
