package gamehub.game_Hub.Module.Report;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import gamehub.game_Hub.Module.User.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "report", schema = "game_hub")
public class Report {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reporter_id")
  private User reporterId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reported_user_id")
  private User reportedUserId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reason_id")
  private ReportReason reason;

  private String message;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "status_id")
  private ReportStatus status;

  @Column(name = "created_at")
  @CreationTimestamp
  private LocalDateTime createdAt;

}
