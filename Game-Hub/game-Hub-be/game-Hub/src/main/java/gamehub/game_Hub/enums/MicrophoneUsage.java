package gamehub.game_Hub.enums;

import lombok.Getter;

@Getter
public enum MicrophoneUsage {

  YES("Voice chat","fa-solid fa-microphone"),
  OPTIONAL("Voice optional","fa-solid fa-headset"),
  NO("No voice","fa-solid fa-microphone-slash");


  private final String microphoneUsageName;
  private final String microphoneUsageIcon;

  MicrophoneUsage(final String microphoneUsageName, final String microphoneUsageIcon) {
    this.microphoneUsageName = microphoneUsageName;
    this.microphoneUsageIcon = microphoneUsageIcon;
  }
}
