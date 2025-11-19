package gamehub.game_Hub.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.Repository.ReportRepository;
import gamehub.game_Hub.Response.ReportReasonResponse;
import gamehub.game_Hub.Service.ReportService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

  private final ReportRepository reportRepository;

  @Override
  public List<ReportReasonResponse> getAllReportReasons() {
    return reportRepository.findAll()
        .stream()
        .map(reportReason -> new ReportReasonResponse(reportReason.getId(), reportReason.getReason(),
            reportReason.getDescription())).toList();
  }

}
