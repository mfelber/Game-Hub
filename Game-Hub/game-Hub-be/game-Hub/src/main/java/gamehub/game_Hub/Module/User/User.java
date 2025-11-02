package gamehub.game_Hub.Module.User;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import gamehub.game_Hub.Module.Badge;
import gamehub.game_Hub.Module.Game;
import gamehub.game_Hub.Module.Genre;
import gamehub.game_Hub.Role.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user", schema = "game_hub")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails, Principal {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String firstName;

  private String lastName;

  private String username;

  private String password;

  @Column(unique = true)
  private String email;

  private String userProfilePicture;

  private String banner;

  private boolean isBanned;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_roles",
      schema = "game_hub",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  private List<Role> roles;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(insertable = false)
  private LocalDateTime lastModifiedAt;

  @ManyToMany
  @JoinTable(name = "user_library", schema = "game_hub",
      inverseJoinColumns = @JoinColumn(name = "game_id"),
      joinColumns = @JoinColumn(name = "user_id"))
  private Set<Game> library;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "user_wishlist", schema = "game_hub",
  inverseJoinColumns = @JoinColumn(name = "game_id"),
  joinColumns = @JoinColumn(name = "user_id"))
  private Set<Game> wishlist;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "user_play_recently", schema = "game_hub",
  joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "game_id"))
  private Set<Game> playRecently;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "user_favorite_game", schema = "game_hub",
      joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "game_id"))
  private Set<Game> favoriteGames;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "user_favorite_genre", schema = "game_hub",
      joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
  private Set<Genre> favoriteGenres;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "user_recommendation_game", schema = "game_hub",
      joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "game_id"))
  private Set<Game> recommendationGames;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "user_badges", schema = "game_hub",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "badge_id"))
  private Set<Badge> badges;

  @Enumerated(EnumType.STRING)
  private Location location;

  @Enumerated(EnumType.STRING)
  private Status status = Status.OFFLINE;

  @Column(name = "bio", length = 2000, nullable = true)
  private String bio;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<PasswordResetToken> passwordResetTokens;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.roles
        .stream()
        .map(role -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toList());
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public String getName() {
    return username;
  }

  public String getFullName() {
    return firstName + " " + lastName;
  }

}
