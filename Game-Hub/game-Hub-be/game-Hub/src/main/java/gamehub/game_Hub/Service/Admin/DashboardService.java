package gamehub.game_Hub.Service.Admin;

import org.springframework.security.core.Authentication;

import gamehub.game_Hub.Response.Admin.DashboardResponse;

public interface DashboardService {

  DashboardResponse loadDashboardData(Authentication connectedUser, int page, int size);

}
