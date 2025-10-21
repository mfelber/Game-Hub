package gamehub.game_Hub.Module;

import java.util.Set;

import org.hibernate.annotations.Fetch;

import com.fasterxml.jackson.annotation.JsonIgnore;

import gamehub.game_Hub.Module.User.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "game", schema = "game_hub")
public class Game {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  private String description;

  @ManyToMany(fetch = FetchType.LAZY)
  @ToString.Exclude
  @JsonIgnore
  @JoinTable(name = "game_genre", schema = "game_hub",
      joinColumns = @JoinColumn(name = "game_id"),
      inverseJoinColumns = @JoinColumn(name = "genre_id"))
  private Set<Genre> genres;

  @ManyToMany(mappedBy = "library",fetch = FetchType.LAZY)
  @Nullable
  @ToString.Exclude
  @JsonIgnore
  private Set<User> owners;

  @OneToMany(mappedBy = "baseGame",fetch = FetchType.LAZY)
  @Nullable
  @JsonIgnore
  @ToString.Exclude
  private Set<DLC> dlcs;

  private String publisher;

  private String developer;

  private String releaseYear;

  private double price;

  private boolean dlc;

  private String imageCover;

}
