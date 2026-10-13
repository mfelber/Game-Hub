package gamehub.game_Hub.enums;

import lombok.Getter;

@Getter
public enum PlayStyle {

  CASUAL("Casual", "I play for fun, with no pressure to win or perform."),
  CHILL("Chill", "Relaxed sessions with plenty of chatting and no stress."),
  COMPETITIVE("Competitive", "I play to win and take the game seriously."),
  HARDCORE("Hardcore", "Long hours, optimization, and pushing for the max."),
  SOCIAL("Social", "I mainly want to talk and meet new people."),
  COMPLETIONIST("Completionist", "I want to finish everything: achievements, story, and side content.");

  private final String label;
  private final String description;

  PlayStyle(String label, String description) {
    this.label = label;
    this.description = description;
  }
}
