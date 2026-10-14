package gamehub.game_Hub.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MicrophoneUsage {

  YES("Voice chat","fa-solid fa-microphone"),
  OPTIONAL("Voice optional","fa-solid fa-headset"),
  NO("No voice","fa-solid fa-microphone-slash");

  private final String microphoneUsageName;
  private final String microphoneUsageIcon;

}
