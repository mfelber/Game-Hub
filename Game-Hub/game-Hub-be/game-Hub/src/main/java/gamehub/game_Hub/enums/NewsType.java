package gamehub.game_Hub.enums;

import lombok.Getter;

@Getter
public enum NewsType {

  ADDED("Added", "fa-solid fa-plus", "text-emerald-400"),
  IMPROVED("Improved", "fa-solid fa-wand-magic-sparkles", "text-blue-400"),
  FIXED("Fixed", "fa-solid fa-bug","text-amber-400"),
  SECURITY("Security", "fa-solid fa-lock","text-violet-400"),
  REMOVED("Removed", "fa-solid fa-trash","text-red-400");


  private final String newsTypeName;
  private final String newsTypeIcon;
  private final String iconColor;

  NewsType(final String newsTypeName, final String newsTypeIcon, final String iconColor) {
    this.newsTypeName = newsTypeName;
    this.newsTypeIcon = newsTypeIcon;
    this.iconColor = iconColor;
  }


}
