package gamehub.game_Hub.Request;

import gamehub.game_Hub.Module.Report.ReportReason;
import gamehub.game_Hub.Module.Report.ReportStatus;
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
public class ReportRequest {

  private Long reason;
  private String message;


}
