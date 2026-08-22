package gamehub.game_Hub.Service;

import java.util.List;

import org.springframework.security.core.Authentication;

import gamehub.game_Hub.Request.ReportRequest;
import gamehub.game_Hub.Response.CommunityGuidelinesResponse;
import jakarta.validation.Valid;

public interface ReportService {

  List<CommunityGuidelinesResponse> getAllCommunityGuidelines();

  Long reportUser(Authentication connectedUser, Long userId, @Valid ReportRequest request);

}
