package gamehub.game_Hub.Response.Admin;

import java.util.Optional;

import gamehub.game_Hub.enums.SuspensionStatus;
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
public class AdminSuspendedAccountsResponse {

  private Long suspensionId;
  private Long reportId;
  private Long userId;
  private String userName;
  private String suspensionReason;
  private String messageFromAdmin;
  private Long suspendedTimes;
  private Long daysSinceLastSuspension;
  private SuspensionStatus suspensionStatus;
  private String expireAt;
//   userSuspensionHistoryResponse
}
