package gamehub.game_Hub.ServiceImpl;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Mapper.LfgMapper;
import gamehub.game_Hub.Module.Game;
import gamehub.game_Hub.Module.User.LfgPost;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Module.UserSavedLfgPost;
import gamehub.game_Hub.Module.embededIds.SavedLfgPostId;
import gamehub.game_Hub.Repository.LfgPostRepository;
import gamehub.game_Hub.Repository.UserSavedLfgPostRepository;
import gamehub.game_Hub.Repository.game.GameRepository;
import gamehub.game_Hub.Repository.user.UserRepository;
import gamehub.game_Hub.Request.LfgCreationRequest;
import gamehub.game_Hub.Response.LanguagesResponse;
import gamehub.game_Hub.Response.LfgResponse;
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

  private static final int FREE_MAX_ACTIVE_POSTS = 3;

  private static final int SUPPORTER_MAX_ACTIVE_POSTS = 5;

  private final UserRepository userRepository;

  private final GameRepository gameRepository;

  private final LfgPostRepository lfgPostRepository;

  private final LfgMapper lfgMapper;

  private final UserSavedLfgPostRepository userSavedLfgPostRepository;

  @Override
  public Long createLfg(final Authentication connectedUser, final LfgCreationRequest request) {

    final boolean flexible = request.flexible();

    if (!flexible) {
      boolean hasDays = request.days() != null && !request.days().isEmpty();
      boolean hasTimeOfDay = request.timeOfDay() != null;

      if (!hasDays || !hasTimeOfDay) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            "Choose days and time of day, or mark the post as flexible.");
      }
    }

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + authUser.getId()));

    Game game = gameRepository.findById(request.gameId())
        .orElseThrow(() -> new EntityNotFoundException("Game with id: " + request.gameId() + " was not found"));

    int activePosts = countActivePosts(user);
    int maxActivePosts = getMaxActivePosts(user);

    if (activePosts >= maxActivePosts) {
      throw new IllegalArgumentException("Free accounts can have only " + maxActivePosts + " active LFG posts.");
    }

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
        .gameTypes(request.gameTypes())
        .voiceChat(request.voiceChat())
        .personality(request.personality())
        .flexible(flexible)
        .days(flexible ? new HashSet<>() : request.days())
        .timeOfDay(flexible ? null : request.timeOfDay())
        .openToRegions(request.openToRegion())
        .languages(request.preferredLanguages())
        .description(request.description().trim())
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

    int activePosts = countActivePosts(user);
    int maxActivePosts = getMaxActivePosts(user);

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
        .timesOfDay(lfgMapper.toIconOptions(TimeOfDay.class))
        .personalities(lfgMapper.toOptions(Personality.class))
        .preferredLanguages(
            Arrays.stream(Languages.values()).map(pl -> new LanguagesResponse(pl.name(), pl.getLabel())).toList())
        .preferredRegions(
            Arrays.stream(Region.values()).map(r -> new RegionResponse(r.name(), r.getRegionName())).toList())
        .postDurations(Arrays.stream(LfgPostDuration.values()).map(d -> new LfgPostDurationResponse(d.name(),
            d.getLabel(), d.getDays(), d.isSupporterOnly(), d.isSupporterOnly() && !user.isSupporter())).toList())
        .canCreate(activePosts < maxActivePosts)
        .activePosts(activePosts)
        .maxActivePosts(maxActivePosts)
        .build();
  }

  @Override
  public PageResponse<LfgResponse> getAllActiveLfgs(final Authentication connectedUser, final int page,
      final int size) {

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

    Page<LfgPost> lfgPosts = lfgPostRepository.findAllByLfgStatus(LfgStatus.ACTIVE, pageable);
    List<LfgResponse> lfgResponse = lfgPosts.stream().map(
        lfg -> lfgMapper.toLfgResponse(lfg)
    ).toList();

    return new PageResponse<>(
        lfgResponse,
        lfgPosts.getNumber(),
        lfgPosts.getSize(),
        lfgPosts.getTotalElements(),
        lfgPosts.getTotalPages(),
        lfgPosts.isFirst(),
        lfgPosts.isLast()
    );
  }

  @Override
  public Integer getCountOfActivePosts(Authentication connectedUser) {

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    return countActivePosts(user);
  }

  @Override
  public Integer getMyLfgPostsLimit(final Authentication connectedUser) {
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    return getMaxActivePosts(user);
  }

  @Override
  public SavedLfgPostId saveLfgPost(final Long lfgPostId, final Authentication connectedUser) {

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    LfgPost lfgPost = lfgPostRepository.findById(lfgPostId)
        .orElseThrow(() -> new EntityNotFoundException("No LFG post with id: " + lfgPostId));

    System.out.println(user.getUsername() + " " + lfgPost.getId());

    if (lfgPostRepository.findByAuthorAndId(user, lfgPost.getId()).isPresent()) {
      throw new IllegalStateException("You cant save your own post");
    }

    if (userSavedLfgPostRepository.findByUserAndLfgPost(user, lfgPost).isPresent()) {
      throw new IllegalStateException("You already saved this post");
    }

    UserSavedLfgPost savedLfgPost = UserSavedLfgPost.builder()
        .id(new SavedLfgPostId(user.getId(), lfgPost.getId()))
        .lfgPost(lfgPost)
        .user(user)
        .savedAt(LocalDateTime.now())
        .build();

    return userSavedLfgPostRepository.save(savedLfgPost).getId();
  }

  private int getMaxActivePosts(final User user) {
    return user.isSupporter() ? SUPPORTER_MAX_ACTIVE_POSTS : FREE_MAX_ACTIVE_POSTS;
  }

  private int countActivePosts(final User user) {
    return Math.toIntExact(lfgPostRepository.countLfgPostByLfgStatusAndAuthor(LfgStatus.ACTIVE, user));
  }

}
