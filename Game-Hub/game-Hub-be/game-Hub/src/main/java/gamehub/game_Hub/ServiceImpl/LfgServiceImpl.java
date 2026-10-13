package gamehub.game_Hub.ServiceImpl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import gamehub.game_Hub.Module.Game;
import gamehub.game_Hub.Module.User.LfgPost;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Repository.LfgPostRepository;
import gamehub.game_Hub.Repository.game.GameRepository;
import gamehub.game_Hub.Repository.user.UserRepository;
import gamehub.game_Hub.Request.LfgCreationRequest;
import gamehub.game_Hub.Service.LfgService;
import gamehub.game_Hub.enums.LfgPostDuration;
import gamehub.game_Hub.enums.LfgStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LfgServiceImpl implements LfgService {

  private final UserRepository userRepository;

  private final GameRepository gameRepository;

  private final LfgPostRepository lfgPostRepository;

  @Override
  public Long createLfg(final Authentication connectedUser, final LfgCreationRequest request) {

    // TODO check how many posts user has FREE account max 3 active SUPPORTER account 5 active

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + authUser.getId()));

    Game game = gameRepository.findById(request.gameId())
        .orElseThrow(() -> new EntityNotFoundException("Game with id: " + request.gameId() + " was not found"));

    LfgPostDuration duration = request.lfgPostDuration();
    if (duration.isSupporterOnly() && !user.isSupporter()) {
      throw new IllegalArgumentException("This duration is available for supporters only");
    }

    Instant now = Instant.now();

    LfgPost lfgPost = LfgPost.builder()
        .author(user)
        .game(game)
        .platforms(request.platforms())
        .lookingFor(request.lookingFor())
        .playStyle(request.playStyle())
        .commitment(request.commitment())
        .voiceChat(request.voiceChat())
        .personality(request.personality())
        .days(request.days())
        .timesOfDay(request.timeOfDay())
        .openToRegions(request.openToRegion())
        .languages(request.preferredLanguages())
        .description(request.description())
        .createdAt(now)
        .expiresAt(now.plus(duration.getDays(), ChronoUnit.DAYS))
        .lfgStatus(LfgStatus.ACTIVE)
        .build();

    return lfgPostRepository.save(lfgPost).getId();
  }

}
