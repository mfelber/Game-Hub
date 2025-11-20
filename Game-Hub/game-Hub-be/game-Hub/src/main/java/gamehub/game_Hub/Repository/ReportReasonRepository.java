package gamehub.game_Hub.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.Report.ReportReason;

public interface ReportReasonRepository extends JpaRepository<ReportReason, Long> {

}
