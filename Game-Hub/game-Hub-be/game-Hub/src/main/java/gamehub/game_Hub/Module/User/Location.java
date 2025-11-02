package gamehub.game_Hub.Module.User;

public enum Location {
  SK("sk.svg"),
  CZ("cz.svg"),
  PL("pl.svg"),
  HU("hu.svg"),
  US("us.svg"),
  UNKNOWN("unknown.svg");

  private final String locationFileName;

  Location (String locationFileName){
    this.locationFileName = locationFileName;
  }



  public String getLocationFileName() {
    return locationFileName;
  }

  public String getLocationIcon() {
    return "/assets/flags/" + locationFileName;
  }
}

