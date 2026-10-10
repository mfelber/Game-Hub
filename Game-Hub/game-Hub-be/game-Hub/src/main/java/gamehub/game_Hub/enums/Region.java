package gamehub.game_Hub.enums;

import lombok.Getter;

@Getter

public enum Region {
  EUROPE("Europe"),
  NA("North America"),
  SA("South America"),
  ASIA("Asia"),
  AFRICA("Africa"),
  ME("Middle East"),
  OC("Oceania"),
  CAB("Central America & Caribbean");

  private final String regionName;

  Region(final String regionName) {
    this.regionName = regionName;
  }
}
