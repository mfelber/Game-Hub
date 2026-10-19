package gamehub.game_Hub.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Languages {

  EN("English"),
  SK("Slovak"),
  CS("Czech"),
  ES("Spanish"),
  DE("German"),
  PT("Portuguese"),
  RU("Russian"),
  FR("French"),
  ZH("Chinese"),
  JA("Japanese"),
  KO("Korean"),
  TR("Turkish"),
  PL("Polish"),
  IT("Italian"),
  AR("Arabic"),
  UK("Ukrainian"),
  NL("Dutch"),
  SV("Swedish"),
  HU("Hungarian");


  private final String label;

}
