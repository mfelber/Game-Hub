package gamehub.game_Hub.ServiceImpl;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import gamehub.game_Hub.Module.Report.CommunityGuidelines;
import gamehub.game_Hub.Module.Report.Report;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Repository.ReportReasonRepository;
import gamehub.game_Hub.Repository.ReportRepository;
import gamehub.game_Hub.Repository.user.UserRepository;
import gamehub.game_Hub.Request.ReportRequest;
import gamehub.game_Hub.Response.ReportReasonResponse;
import gamehub.game_Hub.Service.ReportService;
import gamehub.game_Hub.enums.ReportStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

  private final ReportRepository reportRepository;

  private final UserRepository userRepository;

  private final ReportReasonRepository reportReasonRepository;

  @Override
  public List<ReportReasonResponse> getAllReportReasons() {
    return reportReasonRepository.findAll()
        .stream()
        .map(reportReason -> new ReportReasonResponse(reportReason.getId(), reportReason.getCommunityGuideline(),
            reportReason.getDescription())).toList();
  }

  @Override
  public Long reportUser(final Authentication connectedUser, final Long userId, final ReportRequest request) {
    User authUser = (User) connectedUser.getPrincipal();
    User reporterId = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    User reportedUserId = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + userId));

    CommunityGuidelines reason = reportReasonRepository.findById(request.getReason())
        .orElseThrow(() -> new EntityNotFoundException("No reason found with id: " + request.getReason()));

    var report = Report.builder()
        .reporterId(reporterId)
        .reportedUserId(reportedUserId)
        .reason(reason)
        .status(ReportStatus.NEW)
        .message(request.getMessage())
        .build();

    return reportRepository.save(report).getId();
  }

}
