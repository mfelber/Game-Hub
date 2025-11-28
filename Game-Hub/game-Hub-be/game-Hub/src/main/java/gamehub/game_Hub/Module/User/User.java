package gamehub.game_Hub.Module.User;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import gamehub.game_Hub.Module.Badge;
import gamehub.game_Hub.Module.CardColor;
import gamehub.game_Hub.Module.Game;
import gamehub.game_Hub.Module.Genre;
import gamehub.game_Hub.Module.Level;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "user", schema = "game_hub")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails, Principal {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  private String username;

  private String password;

  @Column(unique = true)
  private String email;

  @Column(name = "bio", length = 2000, nullable = true)
  private String bio;

  @Enumerated(EnumType.STRING)
  private Location location;

  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(name = "user_profile_picture")
  private String userProfilePicture;

  private String banner;

  @Column(name = "banner_type")
  private String bannerType;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "card_color_id")
  private CardColor cardColor;

  @Column(name = "profile_color")
  private String profileColor;

  private Long xp;

  @Enumerated(EnumType.STRING)
  @Column(name = "account_type")
  private AccountType accountType;

  @Column(name = "parent_email")
  private String parentEmail;

  @Column(name = "is_banned")
  private boolean isBanned;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<PasswordResetToken> passwordResetTokens;

  @CreatedDate
  @Column(nullable = false, updatable = false, name = "created_at")
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(insertable = false, name = "last_modified_at")
  private LocalDateTime lastModifiedAt;

  @ManyToOne
  @JoinColumn(name = "level_id")
  private Level level;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_roles",
      schema = "game_hub",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  private List<Role> roles;

  @ManyToMany
  @JoinTable(name = "user_library", schema = "game_hub",
      inverseJoinColumns = @JoinColumn(name = "game_id"),
      joinColumns = @JoinColumn(name = "user_id"))
  private Set<Game> library;

  @ManyToMany
  @JoinTable(name = "user_friends", schema = "game_hub",
      inverseJoinColumns = @JoinColumn(name = "user_id"),
      joinColumns = @JoinColumn(name = "friend_id"))
  private Set<User> friends = new HashSet<>();

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

  public void addFriend(User user) {
    this.friends.add(user);
    user.getFriends().add(this);
  }

}
