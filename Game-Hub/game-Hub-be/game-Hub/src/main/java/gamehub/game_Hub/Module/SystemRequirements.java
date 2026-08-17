package gamehub.game_Hub.Module;


import gamehub.game_Hub.enums.GameUnitSize;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "system_requirements", schema = "game_hub")
public class SystemRequirements {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String cpu;

  private String gpu;

  private Integer ram;

  private Integer storage;

  @Enumerated(EnumType.STRING)
  @Column(name = "game_unit_size")
  private GameUnitSize gameUnitSize;

}