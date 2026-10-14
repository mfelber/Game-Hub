package gamehub.game_Hub.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Languages {

  EN("English"),
  ES("Spanish"),
  PT("Portuguese"),
  RU("Russian"),
  DE("German"),
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
  HU("Hungarian"),
  SK("Slovak"),
  CS("Czech");

  private final String label;

}
