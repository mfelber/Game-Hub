package gamehub.game_Hub.Module.User;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import gamehub.game_Hub.Module.Report.Report;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_warnings", schema = "game_hub")
public class UserWarnings {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User userId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "report_id")
  private Report reportId;

  @Column(name = "admin_msg")
  private String msgFromAdmin;

  @Column(name = "created_at")
  @CreationTimestamp
  private LocalDateTime createdAt;

}
