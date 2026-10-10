package gamehub.game_Hub.enums;

import lombok.Getter;

@Getter
public enum MicrophoneUsage {

  YES("Uses voice chat","fa-solid fa-microphone"),
  NO("No voice chat","fa-solid fa-microphone-slash"),
  OPTIONAL("Voice optional","fa-solid fa-microphone");

  private final String microphoneUsageName;
  private final String microphoneUsageIcon;

  MicrophoneUsage(final String microphoneUsageName, final String microphoneUsageIcon) {
    this.microphoneUsageName = microphoneUsageName;
    this.microphoneUsageIcon = microphoneUsageIcon;
  }
}
