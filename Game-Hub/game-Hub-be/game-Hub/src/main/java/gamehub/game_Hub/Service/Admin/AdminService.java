package gamehub.game_Hub.Service.Admin;

import org.springframework.security.core.Authentication;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Response.Admin.DashboardResponse;
import gamehub.game_Hub.Response.GamePreviewResponse;

public interface AdminService {

  DashboardResponse loadDashboardData(Authentication connectedUser, int page, int size);

  PageResponse<GamePreviewResponse> getAllGames(int page, int size);

}
