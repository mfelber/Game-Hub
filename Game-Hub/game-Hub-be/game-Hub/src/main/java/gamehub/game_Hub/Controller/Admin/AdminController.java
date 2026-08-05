package gamehub.game_Hub.Controller.Admin;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Response.Admin.DashboardResponse;
import gamehub.game_Hub.Service.Admin.DashboardService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("admin")
@RequiredArgsConstructor
public class AdminController {

  private final DashboardService dashboardService;

  @GetMapping("/dashboard")
  public DashboardResponse loadDashboardData (
      @RequestParam(name = "page", defaultValue = "0", required = false) int page,
      @RequestParam(name = "size", defaultValue = "10", required = false) int size,
      Authentication connectedUser){
    return dashboardService.loadDashboardData(connectedUser, page, size);
  }


  // fetch games with gamePreviewResponse with price and discounted price, possibility to add new games
  // fetch users with only name, lastname, username, email, profile pic
  // fetch all reports
  // fetch all genres , possibility to add new genres (not duplicated)
  // fetch all reviews
}
