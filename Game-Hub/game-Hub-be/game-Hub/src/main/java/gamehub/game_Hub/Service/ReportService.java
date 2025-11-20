package gamehub.game_Hub.Service;

import java.util.List;

import org.springframework.security.core.Authentication;

import gamehub.game_Hub.Request.ReportRequest;
import gamehub.game_Hub.Response.ReportReasonResponse;
import jakarta.validation.Valid;

public interface ReportService {

  List<ReportReasonResponse> getAllReportReasons();

  Long reportUser(Authentication connectedUser, Long userId, @Valid ReportRequest request);

}
