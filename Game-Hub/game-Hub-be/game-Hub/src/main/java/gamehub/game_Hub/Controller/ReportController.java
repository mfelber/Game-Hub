package gamehub.game_Hub.Controller;


import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gamehub.game_Hub.Response.ReportReasonResponse;
import gamehub.game_Hub.Service.ReportService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("report")
@RequiredArgsConstructor
public class ReportController {

  private final ReportService reportService;

  @GetMapping("/reasons")
  public List<ReportReasonResponse> getAllReportReasons(){
    return reportService.getAllReportReasons();
  }

}
