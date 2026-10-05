package gamehub.game_Hub.Request;


import gamehub.game_Hub.enums.Country;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateRequest {

  private String firstName;
  private String lastName;
  private String username;
  private String email;
  private String bio;
  private Country country;
  private Long cardColorId;
}
