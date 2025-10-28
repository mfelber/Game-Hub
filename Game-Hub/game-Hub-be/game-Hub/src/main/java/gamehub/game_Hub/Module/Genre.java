package gamehub.game_Hub.Module;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import gamehub.game_Hub.Module.User.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "genre", schema = "game_hub")
public class Genre {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY)
  private Set<Game> games;


  @ManyToMany(mappedBy = "favoriteGenres", fetch = FetchType.LAZY)
  @ToString.Exclude
  @Nullable
  @JsonIgnore
  private Set<User> favoriteGenres;

}
