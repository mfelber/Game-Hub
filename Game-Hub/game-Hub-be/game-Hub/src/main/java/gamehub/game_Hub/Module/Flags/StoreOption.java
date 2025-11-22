package gamehub.game_Hub.Module.Flags;

import lombok.Getter;

@Getter
public enum StoreOption {

  PEGI_16("PEGI 16", "Hides games rated PEGI 16 in the store"),
  PEGI_18("PEGI 18", "Hides games rated PEGI 18 in the store");

  private final String name;
  private final String description;

  StoreOption(final String name, final String description) {
    this.name = name;
    this.description = description;
  }

}
