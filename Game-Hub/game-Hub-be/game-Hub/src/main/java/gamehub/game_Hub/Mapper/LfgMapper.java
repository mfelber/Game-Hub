package gamehub.game_Hub.Mapper;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.File.FileUtils;
import gamehub.game_Hub.Module.Game;
import gamehub.game_Hub.Module.User.LfgPost;
import gamehub.game_Hub.Repository.game.GameRepository;
import gamehub.game_Hub.Response.CountryResponse;
import gamehub.game_Hub.Response.DayAvailabilityResponse;
import gamehub.game_Hub.Response.LanguagesResponse;
import gamehub.game_Hub.Response.LfgResponse;
import gamehub.game_Hub.Response.MicrophoneUsageResponse;
import gamehub.game_Hub.Response.PlatformResponse;
import gamehub.game_Hub.Response.RegionResponse;
import gamehub.game_Hub.Response.lfg.LfgIconOptionResponse;
import gamehub.game_Hub.Response.lfg.LfgOptionResponse;
import gamehub.game_Hub.enums.IconLfgOption;
import gamehub.game_Hub.enums.Languages;
import gamehub.game_Hub.enums.Lfg.GameType;
import gamehub.game_Hub.enums.Lfg.LookingFor;
import gamehub.game_Hub.enums.LfgOption;
import gamehub.game_Hub.enums.MicrophoneUsage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LfgMapper {

  private final GameRepository gameRepository;

  public <E extends Enum<E> & LfgOption> List<LfgOptionResponse> toOptions(Class<E> type) {
    return Arrays.stream(type.getEnumConstants())
        .map(e -> new LfgOptionResponse(e.name(), e.getLabel(), e.getDescription()))
        .toList();
  }

  public <E extends Enum<E> & IconLfgOption> List<LfgIconOptionResponse> toIconOptions(Class<E> type) {
    return Arrays.stream(type.getEnumConstants())
        .map(e -> new LfgIconOptionResponse(e.name(), e.getLabel(), e.getDescription(), e.getIcon()))
        .toList();
  }

  public <E extends Enum<E> & IconLfgOption> LfgIconOptionResponse toIconLfgEnumResponse(E e) {
    if (e == null) return null;
    return new LfgIconOptionResponse(e.name(), e.getLabel(), e.getDescription(), e.getIcon());
  }

  public <E extends Enum<E> & LfgOption> LfgOptionResponse toLfgEnumResponse(E e) {
    if (e == null) return null;
    return new LfgOptionResponse(e.name(), e.getLabel(), e.getDescription());
  }


  public LfgIconOptionResponse  toLookigForResponse (LookingFor lookingFor) {
    return new LfgIconOptionResponse(
        lookingFor.name(),
        lookingFor.getLabel(),
        lookingFor.getDescription(),
        lookingFor.getIcon()
    );
  }

  private List<LfgOptionResponse> toGameTypeResponses(Set<GameType> gameTypes) {
    if (gameTypes == null) return List.of();
    return gameTypes.stream()
        .sorted()
        .map(this::toLfgEnumResponse)
        .toList();
  }

  public MicrophoneUsageResponse toVoiceChatResponse (MicrophoneUsage voiceChat) {
    return MicrophoneUsageResponse.builder()
        .name(voiceChat.getMicrophoneUsageName())
        .icon(voiceChat.getMicrophoneUsageIcon())
        .build();
  }

  public LanguagesResponse toLanguagesResponse (Languages language) {
    return LanguagesResponse.builder()
        .label(language.getLabel())
        .build();
  }

  public List<DayAvailabilityResponse> toDayAvailability (Set<DayOfWeek> selectedDays) {
    Set<DayOfWeek> selected = selectedDays == null ? Set.of() : selectedDays;
    return Arrays.stream(DayOfWeek.values()).map(day -> new DayAvailabilityResponse(
        day.name(),
        day.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
        selected.contains(day)
    )).toList();
  }
  // time of day and days can be null, gametypes could be null, languages, description, personality

  public LfgResponse toLfgResponse(LfgPost post) {

    return LfgResponse.builder()
        .lfgId(post.getId())
        .username(post.getAuthor().getName())
        .userRegion(new RegionResponse(post.getAuthor().getRegion().name(), post.getAuthor().getRegion().getRegionName()))
        .country(new CountryResponse(post.getAuthor().getCountry().name(), post.getAuthor().getCountry().getCountryName(), post.getAuthor().getCountry().getCountryIcon()))
        .userProfilePicture(FileUtils.readCoverFromLocation(post.getAuthor().getUserProfilePicture()))
        .profileColor(post.getAuthor().getProfileColor())
        .onlineStatus(post.getAuthor().getStatus())
        .gameName(post.getGame().getTitle())
        .lookingFor(toIconLfgEnumResponse(post.getLookingFor()))
        .commitment(toLfgEnumResponse(post.getCommitment()))
        .lfgDescription(post.getDescription())
        .gameTypes(toGameTypeResponses(post.getGameTypes()))
        .platform(post.getPlatforms().stream().map(pl -> new PlatformResponse(pl.name(), pl.getPlatformName(), pl.getPlatformIcon())).toList())
        .microphoneUsage(toVoiceChatResponse(post.getVoiceChat()))
        .languages(post.getLanguages().stream().map(this::toLanguagesResponse).toList())
        .playStyle(toIconLfgEnumResponse(post.getPlayStyle()))
        .personality(toLfgEnumResponse(post.getPersonality()))
        .openToRegions(post.getOpenToRegions().stream().map(reg -> new RegionResponse(reg.name(), reg.getRegionName())).toList())
        .days(toDayAvailability(post.getDays()))
        .timeOfDay(toIconLfgEnumResponse(post.getTimeOfDay()))
        .flexible(post.isFlexible())
        .lfgStatus(post.getLfgStatus())
        .createdAt(post.getCreatedAt())
        .build();
  }

}
