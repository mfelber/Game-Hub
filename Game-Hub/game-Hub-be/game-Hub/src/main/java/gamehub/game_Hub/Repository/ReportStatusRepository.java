package gamehub.game_Hub.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.Report.ReportStatus;

public interface ReportStatusRepository extends JpaRepository<ReportStatus, Long> {

}
