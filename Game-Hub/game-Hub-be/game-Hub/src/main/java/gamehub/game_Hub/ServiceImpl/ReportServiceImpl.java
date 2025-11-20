package gamehub.game_Hub.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import gamehub.game_Hub.Module.Report.Report;
import gamehub.game_Hub.Module.Report.ReportReason;
import gamehub.game_Hub.Module.Report.ReportStatus;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Repository.ReportReasonRepository;
import gamehub.game_Hub.Repository.ReportRepository;
import gamehub.game_Hub.Repository.ReportStatusRepository;
import gamehub.game_Hub.Repository.user.UserRepository;
import gamehub.game_Hub.Request.ReportRequest;
import gamehub.game_Hub.Response.ReportReasonResponse;
import gamehub.game_Hub.Service.ReportService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

  private final ReportRepository reportRepository;

  private final UserRepository userRepository;

  private final ReportReasonRepository reportReasonRepository;

  private final ReportStatusRepository reportStatusRepository;

  @Override
  public List<ReportReasonResponse> getAllReportReasons() {
    return reportReasonRepository.findAll()
        .stream()
        .map(reportReason -> new ReportReasonResponse(reportReason.getId(), reportReason.getReason(),
            reportReason.getDescription())).toList();
  }

  @Override
  public Long reportUser(final Authentication connectedUser, final Long userId, final ReportRequest request) {
    User authUser = (User) connectedUser.getPrincipal();
    User reporterId = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    User reportedUserId = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + userId));

    ReportReason reason = reportReasonRepository.findById(request.getReason())
        .orElseThrow(() -> new EntityNotFoundException("No reason found with id: " + request.getReason()));

    ReportStatus reportStatus = reportStatusRepository.findById(1L)
        .orElseThrow(() -> new EntityNotFoundException("No report status found with id: " + 1));


    var report = Report.builder()
        .reporterId(reporterId)
        .reportedUserId(reportedUserId)
        .reason(reason)
        .status(reportStatus)
        .message(request.getMessage())
        .build();

    Report saved = reportRepository.save(report);

    return report.getId();
  }

}
