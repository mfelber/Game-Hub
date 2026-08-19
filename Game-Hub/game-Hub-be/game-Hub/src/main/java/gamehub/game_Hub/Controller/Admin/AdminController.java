package gamehub.game_Hub.Controller.Admin;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Request.BanUserRequest;
import gamehub.game_Hub.Request.GameRequest;
import gamehub.game_Hub.Request.GameUpdateRequest;
import gamehub.game_Hub.Response.Admin.AccountStatusResponse;
import gamehub.game_Hub.Response.Admin.AdminUserResponse;
import gamehub.game_Hub.Response.Admin.DashboardResponse;
import gamehub.game_Hub.Response.Admin.RoleResponse;
import gamehub.game_Hub.Response.GamePreviewResponse;
import gamehub.game_Hub.Response.GameResponse;
import gamehub.game_Hub.Service.Admin.AdminService;
import gamehub.game_Hub.Service.GameService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("admin")
@RequiredArgsConstructor
public class AdminController {

  private final AdminService adminService;

  private final GameService gameService;

  // Load admin dashboard
  @GetMapping("/dashboard")
  public DashboardResponse loadDashboardData(
      @RequestParam(name = "page", defaultValue = "0", required = false) int page,
      @RequestParam(name = "size", defaultValue = "10", required = false) int size,
      Authentication connectedUser) {
    return adminService.loadDashboardData(connectedUser, page, size);
  }

  // Fetch games for admin
  @GetMapping("/admin/games")
  public ResponseEntity<PageResponse<GamePreviewResponse>> getAllGames(
      @RequestParam(name = "page", defaultValue = "0", required = false) int page,
      @RequestParam(name = "size", defaultValue = "50", required = false) int size) {
    return ResponseEntity.ok(adminService.getAllGames(page, size));
  }

  // fetch users with only name, lastname, username, email, profile pic, role, registered, lastLogin, accountStatus
  @GetMapping("/users")
  public ResponseEntity<PageResponse<AdminUserResponse>> getAllUsers(
      @RequestParam(name = "page", defaultValue = "0", required = false) int page,
      @RequestParam(name = "page", defaultValue = "50", required = false) int size
  ) {
    return ResponseEntity.ok(adminService.getAllUsers(page, size));
  }
  // fetch all reports
  // fetch all genres , possibility to add new genres (not duplicated)
  // fetch all reviews

  @GetMapping("/game/info/{gameId}")
  public GameResponse getGameInfo(@PathVariable Long gameId) {
    return adminService.getGameInfo(gameId);
  }

  @DeleteMapping("/game/delete/{gameId}")
  public void deleteGame(@PathVariable Long gameId) {
    adminService.deleteGame(gameId);
  }

  // Add game to the store
  @PostMapping("/add-game")
  public ResponseEntity<Long> addGame(@Valid @RequestBody GameRequest gameRequest) {
    return ResponseEntity.ok(gameService.save(gameRequest));
  }

  // Upload game cover picture
  @PostMapping(value = "/cover/{gameId}", consumes = "multipart/form-data")
  public ResponseEntity<?> uploadGameCoverImage(
      @PathVariable final Long gameId,
      @Parameter()
      @RequestPart("file") MultipartFile file,
      final Authentication connectedUser
  ) {
    gameService.uploadGameCoverImage(gameId, file);
    return ResponseEntity.accepted().build();
  }

  @PutMapping("/edit-game/{gameId}")
  public ResponseEntity<Long> updateGame(@PathVariable Long gameId,
      @Valid @RequestBody GameUpdateRequest gameUpdateRequest) {
    return ResponseEntity.ok(gameService.update(gameId, gameUpdateRequest));
  }

  @GetMapping("/user/info/{userId}")
  public AdminUserResponse getUserInfo(@PathVariable Long userId) {
    return adminService.getUserInfo(userId);
  }

  @PutMapping("/change-role/{userId}")
  public ResponseEntity<Long> changeRole(@PathVariable Long userId) {
    return ResponseEntity.ok(adminService.changeRole(userId));
  }

  @PutMapping("/ban/{userId}")
  public ResponseEntity<Long> banUser(@PathVariable Long userId, @RequestBody @Valid BanUserRequest banUserRequest)
      throws MessagingException {
    return ResponseEntity.ok(adminService.banUser(userId, banUserRequest));
  }

  @PutMapping("/unban/{userId}")
  public ResponseEntity<Long> unBanUser(@PathVariable Long userId) {
    return ResponseEntity.ok(adminService.unBanUser(userId));
  }




  @GetMapping("/roles")
  public ResponseEntity<List<RoleResponse>> getAllRoles() {
    return ResponseEntity.ok(adminService.getAllRoles());
  }

  @GetMapping("/account-statuses")
  public ResponseEntity<List<AccountStatusResponse>> getAllAccountStatuses() {
    return ResponseEntity.ok(adminService.getAllAccountStatuses());
  }
}
