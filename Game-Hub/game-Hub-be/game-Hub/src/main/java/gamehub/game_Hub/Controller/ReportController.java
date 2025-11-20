package gamehub.game_Hub.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gamehub.game_Hub.Request.ReportRequest;
import gamehub.game_Hub.Response.ReportReasonResponse;
import gamehub.game_Hub.Service.ReportService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("report")
@RequiredArgsConstructor
public class ReportController {

  private final ReportService reportService;

  @GetMapping("/get/reasons")
  public List<ReportReasonResponse> getAllReportReasons() {
    return reportService.getAllReportReasons();
  }

  @PostMapping("/user/{userId}")
  public ResponseEntity<Long> reportUser(Authentication connectedUser, @PathVariable final Long userId,
      @RequestBody @Valid final ReportRequest request) {

    return ResponseEntity.ok(reportService.reportUser(connectedUser, userId, request));
  }

}
