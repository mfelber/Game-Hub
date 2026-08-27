package gamehub.game_Hub.Response.Admin;

import java.time.LocalDateTime;
import java.util.List;

import gamehub.game_Hub.enums.AccountStatus;
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
public class AdminUserModerationResponse {

  private Long userId;
  private String userName;
  private AccountStatus accountStatus;
  private LocalDateTime registeredAt;

  private Long reportCount;
  private Long warningCount;
  private Long suspensionCount;
  private Long banCount;

  private List<AdminReportsResponse> reports;
  private List<AdminWarningResponse> warnings;
  private List<AdminSuspendedAccountsResponse> suspensions;
  private List<AdminBanResponse> bans;

}
