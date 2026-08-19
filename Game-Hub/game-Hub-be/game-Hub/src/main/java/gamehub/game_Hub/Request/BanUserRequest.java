package gamehub.game_Hub.Request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BanUserRequest {

  @NotNull
  private Long banReason;

  private String customMessage;

}


