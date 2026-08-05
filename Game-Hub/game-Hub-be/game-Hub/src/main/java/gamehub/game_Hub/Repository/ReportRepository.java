package gamehub.game_Hub.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.Report.Report;
import gamehub.game_Hub.Module.Report.ReportStatus;
import gamehub.game_Hub.Module.User.User;

public interface ReportRepository extends JpaRepository<Report, Long> {

  Long countReportsByStatus_Id(long statusId);

  List<Report> findTop5ByOrderByCreatedAtDesc();

}
