package gamehub.game_Hub.Repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.User.PasswordResetToken;
import gamehub.game_Hub.Module.User.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

  PasswordResetToken findByToken(String token);

  PasswordResetToken findByUser(User user);

}
