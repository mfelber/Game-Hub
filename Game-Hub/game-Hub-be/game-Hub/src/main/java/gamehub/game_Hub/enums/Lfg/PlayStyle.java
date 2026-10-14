package gamehub.game_Hub.enums.Lfg;

import gamehub.game_Hub.enums.IconLfgOption;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlayStyle implements IconLfgOption {

  CASUAL("Casual", "I play for fun, with no pressure to win or perform.", "fa-regular fa-face-smile-beam"),
  CHILL("Chill", "Relaxed sessions with plenty of chatting and no stress.", "fa-solid fa-mug-hot"),
  COMPETITIVE("Competitive", "I play to win and take the game seriously.", "fa-solid fa-ranking-star"),
  HARDCORE("Hardcore", "Long hours, optimization, and pushing for the max.", "fa-solid fa-skull"),
  SOCIAL("Social", "I mainly want to talk and meet new people.", "fa-solid fa-comments"),
  COMPLETIONIST("Completionist", "I want to finish everything: achievements, story, and side content.", "fa-solid fa-trophy");

  private final String label;
  private final String description;
  private final String icon;
}
