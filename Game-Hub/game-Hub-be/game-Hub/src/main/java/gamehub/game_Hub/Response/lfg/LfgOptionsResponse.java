package gamehub.game_Hub.Response.lfg;

import java.time.DayOfWeek;
import java.util.List;

import gamehub.game_Hub.Response.LanguageResponse;
import gamehub.game_Hub.Response.LanguagesResponse;
import gamehub.game_Hub.Response.MicrophoneUsageResponse;
import gamehub.game_Hub.Response.PlatformResponse;
import gamehub.game_Hub.Response.RegionResponse;
import gamehub.game_Hub.enums.MicrophoneUsage;
import lombok.Builder;

@Builder
public record LfgOptionsResponse(
    List<LfgIconOptionResponse> lookingFors,
    List<LfgOptionResponse> commitments,
    List<PlatformResponse> platforms,
    List<MicrophoneUsageResponse> voiceChats,
    List<LfgIconOptionResponse> playStyles,
    List<LfgOptionResponse> gameTypes,
    List<LfgOptionResponse> days,
    List<LfgOptionResponse> timesOfDay,
    List<LfgOptionResponse> personalities,
    List<LanguagesResponse> preferredLanguages,
    List<RegionResponse> preferredRegions,
    List<LfgPostDurationResponse> postDurations
) {

}
