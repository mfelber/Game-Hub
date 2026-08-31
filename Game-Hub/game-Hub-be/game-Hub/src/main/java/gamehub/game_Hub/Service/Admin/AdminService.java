package gamehub.game_Hub.Service.Admin;

import java.util.List;

import org.springframework.security.core.Authentication;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Request.BanUserRequest;
import gamehub.game_Hub.Request.SuspendAccountRequest;
import gamehub.game_Hub.Request.WarnUserRequest;
import gamehub.game_Hub.Response.Admin.AccountStatusResponse;
import gamehub.game_Hub.Response.Admin.AdminReportsResponse;
import gamehub.game_Hub.Response.Admin.AdminSuspendedAccountsResponse;
import gamehub.game_Hub.Response.Admin.AdminUserModerationResponse;
import gamehub.game_Hub.Response.Admin.AdminUserResponse;
import gamehub.game_Hub.Response.Admin.DashboardResponse;
import gamehub.game_Hub.Response.Admin.ReportStatusResponse;
import gamehub.game_Hub.Response.Admin.RoleResponse;
import gamehub.game_Hub.Response.GamePreviewResponse;
import gamehub.game_Hub.Response.GameResponse;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

public interface AdminService {

  DashboardResponse loadDashboardData(Authentication connectedUser, int page, int size);

  PageResponse<GamePreviewResponse> getAllGames(int page, int size);

  GameResponse getGameInfo(Long gameId);

  void deleteGame(Long gameId);

  PageResponse<AdminUserResponse> getAllUsers(int page, int size);

  List<RoleResponse> getAllRoles();

  List<AccountStatusResponse> getAllAccountStatuses();

  AdminUserResponse getUserInfo(Long userId);

  Long changeRole(Long userId);

  Long banUser(Long userId, BanUserRequest banUserRequest) throws MessagingException;

  Long unBanUser(Long userId) throws MessagingException;

  PageResponse<AdminReportsResponse> getAllReports(int page, int size);

  List<ReportStatusResponse> getAllReportStatuses();

  Long suspendAccount(Long userId, SuspendAccountRequest suspendAccountRequest) throws MessagingException;

  Long changeStatusInReview(Long reportId);

  PageResponse<AdminSuspendedAccountsResponse> getAllSuspendedAccounts(int page, int size);

  Long noActionOnReportedUser(Long reportId);

  Long warnUser(Long userId, @Valid WarnUserRequest warnUserRequest);

  Long rejectReport(Long reportId);

  AdminUserModerationResponse getSuspendedUserDetails(Long userId);

}
