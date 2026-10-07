package gamehub.game_Hub.enums;

import lombok.Getter;

@Getter
public enum NewsType {

  ADDED("Added", "fa-solid fa-plus"),
  IMPROVED("Improved", "fa-solid fa-wand-magic-sparkles"),
  FIXED("Fixed", "fa-solid fa-bug"),
  SECURITY("Security", "fa-solid fa-lock"),
  REMOVED("Removed", "fa-solid fa-trash");


  private final String newsTypeName;
  private final String newsTypeIcon;

  NewsType(final String newsTypeName, final String newsTypeIcon) {
    this.newsTypeName = newsTypeName;
    this.newsTypeIcon = newsTypeIcon;
  }


}
