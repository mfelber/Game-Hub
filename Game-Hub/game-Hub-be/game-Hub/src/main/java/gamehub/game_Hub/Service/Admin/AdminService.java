package gamehub.game_Hub.Service.Admin;

import org.springframework.security.core.Authentication;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Response.Admin.AdminUserResponse;
import gamehub.game_Hub.Response.Admin.DashboardResponse;
import gamehub.game_Hub.Response.GamePreviewResponse;
import gamehub.game_Hub.Response.GameResponse;

public interface AdminService {

  DashboardResponse loadDashboardData(Authentication connectedUser, int page, int size);

  PageResponse<GamePreviewResponse> getAllGames(int page, int size);

  GameResponse getGameInfo(Long gameId);

  void deleteGame(Long gameId);

  PageResponse<AdminUserResponse> getAllUsers(int page, int size);

}
