package gamehub.game_Hub.Repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.Report.Report;
import gamehub.game_Hub.enums.ReportStatus;

public interface ReportRepository extends JpaRepository<Report, Long> {

  List<Report> findTop5ByOrderByCreatedAtDesc();

  List<Report> findAllByStatus(ReportStatus status);

  Long countReportsByStatusIn(Collection<ReportStatus> statuses);

}
