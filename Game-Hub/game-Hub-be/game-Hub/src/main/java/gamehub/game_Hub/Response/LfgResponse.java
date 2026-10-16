package gamehub.game_Hub.Response;

import java.time.DayOfWeek;
import java.util.List;

import gamehub.game_Hub.Response.lfg.LfgIconOptionResponse;
import gamehub.game_Hub.Response.lfg.LfgOptionsResponse;
import gamehub.game_Hub.enums.Lfg.Commitment;
import gamehub.game_Hub.enums.Lfg.LookingFor;
import gamehub.game_Hub.enums.MicrophoneUsage;
import gamehub.game_Hub.enums.Platform;
import gamehub.game_Hub.enums.Status;
import lombok.Builder;

@Builder
public record LfgResponse(

    Long lfgId,
    String username,
    byte [] userProfilePicture,
    Status onlineStatus,
    String gameName,
    LfgIconOptionResponse lookingFor,
    LfgOptionsResponse commitment,
    String lfgDescription,
    List<LfgOptionsResponse> gameTypes,
    PlatformResponse platform,
    MicrophoneUsageResponse microphoneUsage,
    LanguagesResponse languages,
    LfgIconOptionResponse playStyle,
    LfgOptionsResponse personality,
    RegionResponse preferredRegions,
    DayOfWeek days,
    LfgIconOptionResponse timeOfDay
) {

}
