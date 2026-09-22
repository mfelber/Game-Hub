package gamehub.game_Hub.enums;

public enum Country {
  SK("sk.svg", "Slovakia"),
  CZ("cz.svg", "Czech Republic"),
  PL("pl.svg", "Poland"),
  HU("hu.svg", "Hungary"),
  US("us.svg", "United States"),
  NOT_SPECIFIED("not_specified.svg","Not Selected");

  private final String countryFileName;
  private final String countryName;

  Country(final String countryFileName, final String countryName){
    this.countryFileName = countryFileName;
    this.countryName = countryName;
  }

  public String getCountryName() {
    return countryName;
  }


  public String getCountryFileName() {
    return countryFileName;
  }

  public String getCountryIcon() {
    return "/assets/flags/" + countryFileName;
  }
}

