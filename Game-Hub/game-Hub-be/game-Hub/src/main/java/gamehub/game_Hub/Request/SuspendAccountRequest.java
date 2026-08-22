package gamehub.game_Hub.Request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SuspendAccountRequest {

  @NotNull
  private Long suspendReason;

  @NotNull
  private String customMessage;

  @NotNull
  private String expiresAt;

}
