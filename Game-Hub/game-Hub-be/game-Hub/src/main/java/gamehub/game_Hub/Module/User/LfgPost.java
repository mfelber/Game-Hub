package gamehub.game_Hub.Module.User;

import java.time.DayOfWeek;
import java.time.Instant;
import java.util.Set;

import gamehub.game_Hub.Module.Game;
import gamehub.game_Hub.enums.Lfg.Commitment;
import gamehub.game_Hub.enums.Languages;
import gamehub.game_Hub.enums.Lfg.GameType;
import gamehub.game_Hub.enums.Lfg.LookingFor;
import gamehub.game_Hub.enums.Lfg.LfgStatus;
import gamehub.game_Hub.enums.MicrophoneUsage;
import gamehub.game_Hub.enums.Lfg.Personality;
import gamehub.game_Hub.enums.Platform;
import gamehub.game_Hub.enums.Lfg.PlayStyle;
import gamehub.game_Hub.enums.Region;
import gamehub.game_Hub.enums.Lfg.TimeOfDay;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "lfg_post", schema = "game_hub")
public class LfgPost {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id", nullable = false)
  private User author;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "game_id", nullable = false)
  private Game game;

  @ElementCollection
  @CollectionTable(name = "lfg_post_platforms", schema = "game_hub", joinColumns = @JoinColumn(name = "lfg_post_id"))
  @Column(name = "platform")
  @Enumerated(EnumType.STRING)
  private Set<Platform> platforms;

  @Enumerated(EnumType.STRING)
  @Column(name = "looking_for")
  private LookingFor lookingFor;

  @Enumerated(EnumType.STRING)
  @Column(name = "play_style")
  private PlayStyle playStyle;

  @ElementCollection
  @CollectionTable(name = "lfg_post_game_types", schema = "game_hub", joinColumns = @JoinColumn(name = "lfg_post_id"))
  @Column(name = "game_type")
  @Enumerated(EnumType.STRING)
  private Set<GameType> gameTypes;

  @Enumerated(EnumType.STRING)
  private Commitment commitment;

  @Enumerated(EnumType.STRING)
  @Column(name = "voice_chat")
  private MicrophoneUsage voiceChat;

  @Enumerated(EnumType.STRING)
  private Personality personality;

  @ElementCollection
  @CollectionTable(name = "lfg_post_days", schema = "game_hub", joinColumns = @JoinColumn(name = "lfg_post_id"))
  @Column(name = "day")
  @Enumerated(EnumType.STRING)
  private Set<DayOfWeek> days;

  @ElementCollection
  @CollectionTable(name = "lfg_post_times_of_day", schema = "game_hub", joinColumns = @JoinColumn(name = "lfg_post_id"))
  @Column(name = "time_of_day")
  @Enumerated(EnumType.STRING)
  private Set<TimeOfDay> timesOfDay;

  @Column(nullable = false)
  private boolean flexible;

  @ElementCollection
  @CollectionTable(name = "lfg_post_open_to_regions", schema = "game_hub", joinColumns = @JoinColumn(name = "lfg_post_id"))
  @Column(name = "region")
  @Enumerated(EnumType.STRING)
  private Set<Region> openToRegions;

  @ElementCollection
  @CollectionTable(name = "lfg_post_languages", schema = "game_hub", joinColumns = @JoinColumn(name = "lfg_post_id"))
  @Column(name = "language")
  @Enumerated(EnumType.STRING)
  private Set<Languages> languages;


  @Column(length = 300)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "lfg_status")
  private LfgStatus lfgStatus;

  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "expires_at")
  private Instant expiresAt;

}
