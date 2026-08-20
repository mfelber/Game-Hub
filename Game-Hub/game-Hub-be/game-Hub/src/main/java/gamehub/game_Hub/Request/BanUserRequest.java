package gamehub.game_Hub.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BanUserRequest {

  @NotNull
  private Long banReason;

  @Schema(nullable = true)
  private String customMessage;

}