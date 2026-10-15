package gamehub.game_Hub.ServiceImpl;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Locale;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import gamehub.game_Hub.Mapper.LfgMapper;
import gamehub.game_Hub.Module.Game;
import gamehub.game_Hub.Module.User.LfgPost;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Repository.LfgPostRepository;
import gamehub.game_Hub.Repository.game.GameRepository;
import gamehub.game_Hub.Repository.user.UserRepository;
import gamehub.game_Hub.Request.LfgCreationRequest;
import gamehub.game_Hub.Response.LanguagesResponse;
import gamehub.game_Hub.Response.MicrophoneUsageResponse;
import gamehub.game_Hub.Response.PlatformResponse;
import gamehub.game_Hub.Response.RegionResponse;
import gamehub.game_Hub.Response.lfg.LfgOptionResponse;
import gamehub.game_Hub.Response.lfg.LfgOptionsResponse;
import gamehub.game_Hub.Response.lfg.LfgPostDurationResponse;
import gamehub.game_Hub.Service.LfgService;
import gamehub.game_Hub.enums.Languages;
import gamehub.game_Hub.enums.Lfg.Commitment;
import gamehub.game_Hub.enums.Lfg.GameType;
import gamehub.game_Hub.enums.Lfg.LookingFor;
import gamehub.game_Hub.enums.Lfg.LfgPostDuration;
import gamehub.game_Hub.enums.Lfg.LfgStatus;
import gamehub.game_Hub.enums.Lfg.Personality;
import gamehub.game_Hub.enums.Lfg.PlayStyle;
import gamehub.game_Hub.enums.Lfg.TimeOfDay;
import gamehub.game_Hub.enums.MicrophoneUsage;
import gamehub.game_Hub.enums.Platform;
import gamehub.game_Hub.enums.Region;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LfgServiceImpl implements LfgService {

  private final UserRepository userRepository;

  private final GameRepository gameRepository;

  private final LfgPostRepository lfgPostRepository;

  private final LfgMapper lfgMapper;

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

    // check if flexible true days, times of day set to null
    // and set flexible based on response

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

  @Override
  public LfgOptionsResponse getLfgOptions(Authentication connectedUser) {

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    return LfgOptionsResponse.builder()
        .lookingFors(lfgMapper.toIconOptions(LookingFor.class))
        .commitments(lfgMapper.toOptions(Commitment.class))
        .platforms(Arrays.stream(Platform.values())
            .map(platform -> new PlatformResponse(platform.name(),
                platform.getPlatformName(), platform.getPlatformIcon()))
            .toList())
        .voiceChats(Arrays.stream(MicrophoneUsage.values())
            .map(microphoneUsage -> new MicrophoneUsageResponse(microphoneUsage.name(),
                microphoneUsage.getMicrophoneUsageName(), microphoneUsage.getMicrophoneUsageIcon()))
            .toList())
        .playStyles(lfgMapper.toIconOptions(PlayStyle.class))
        .gameTypes(lfgMapper.toOptions(GameType.class))
        .days(
            Arrays.stream(DayOfWeek.values()).map(d -> new LfgOptionResponse(d.name(), d.getDisplayName(TextStyle.FULL,
                Locale.ENGLISH), d.getDisplayName(TextStyle.SHORT, Locale.ENGLISH))).toList())
        .timesOfDay(lfgMapper.toOptions(TimeOfDay.class))
        .personalities(lfgMapper.toOptions(Personality.class))
        .preferredLanguages(
            Arrays.stream(Languages.values()).map(pl -> new LanguagesResponse(pl.name(), pl.getLabel())).toList())
        .preferredRegions(Arrays.stream(Region.values()).map(r -> new RegionResponse(r.name(), r.getRegionName())).toList())
        .postDurations(Arrays.stream(LfgPostDuration.values()).map(d -> new LfgPostDurationResponse(d.name(),
            d.getLabel(), d.getDays(), d.isSupporterOnly(), d.isSupporterOnly() && !user.isSupporter())).toList())
        .build();
  }

}
