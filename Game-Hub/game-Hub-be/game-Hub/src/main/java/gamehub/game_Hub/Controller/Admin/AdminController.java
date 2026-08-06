package gamehub.game_Hub.Controller.Admin;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Response.Admin.DashboardResponse;
import gamehub.game_Hub.Response.GamePreviewResponse;
import gamehub.game_Hub.Service.Admin.AdminService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("admin")
@RequiredArgsConstructor
public class AdminController {

  private final AdminService adminService;

  // Load admin dashboard
  @GetMapping("/dashboard")
  public DashboardResponse loadDashboardData (
      @RequestParam(name = "page", defaultValue = "0", required = false) int page,
      @RequestParam(name = "size", defaultValue = "10", required = false) int size,
      Authentication connectedUser){
    return adminService.loadDashboardData(connectedUser, page, size);
  }

  // Fetch games for admin
  @GetMapping("/admin/games")
  public ResponseEntity<PageResponse<GamePreviewResponse>> getAllGames(
      @RequestParam(name = "page", defaultValue = "0", required = false) int page,
      @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
    return ResponseEntity.ok(adminService.getAllGames(page, size));
  }
  // fetch users with only name, lastname, username, email, profile pic
  // fetch all reports
  // fetch all genres , possibility to add new genres (not duplicated)
  // fetch all reviews
}
