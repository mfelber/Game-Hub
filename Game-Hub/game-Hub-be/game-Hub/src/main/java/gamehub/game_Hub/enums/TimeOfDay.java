package gamehub.game_Hub.enums;

import lombok.Getter;

@Getter
public enum TimeOfDay {

  MORNING("Morning", "6:00 – 12:00"),
  AFTERNOON("Afternoon", "12:00 – 18:00"),
  EVENING("Evening", "18:00 – 23:00"),
  NIGHT("Night", "23:00 – 6:00");

  private final String label;
  private final String hours;


  TimeOfDay(String label, String hours) {
    this.label = label;
    this.hours = hours;
  }

}
