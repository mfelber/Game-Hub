package gamehub.game_Hub.Response;

import java.time.DayOfWeek;
import java.time.Instant;
import java.util.List;

import gamehub.game_Hub.Response.lfg.LfgIconOptionResponse;
import gamehub.game_Hub.Response.lfg.LfgOptionResponse;
import gamehub.game_Hub.Response.lfg.LfgOptionsResponse;
import gamehub.game_Hub.enums.Lfg.Commitment;
import gamehub.game_Hub.enums.Lfg.LfgStatus;
import gamehub.game_Hub.enums.Lfg.LookingFor;
import gamehub.game_Hub.enums.MicrophoneUsage;
import gamehub.game_Hub.enums.Platform;
import gamehub.game_Hub.enums.Status;
import lombok.Builder;

@Builder
public record LfgResponse(

    Long lfgId,
    String username,
    RegionResponse userRegion,
    CountryResponse country,
    byte [] userProfilePicture,
    String profileColor,
    Status onlineStatus,
    String gameName,
    LfgIconOptionResponse lookingFor,
    LfgOptionResponse commitment,
    String lfgDescription,
    List <LfgOptionResponse> gameTypes,
    List <PlatformResponse> platform,
    MicrophoneUsageResponse microphoneUsage,
    List <LanguagesResponse> languages,
    LfgIconOptionResponse playStyle,
    LfgOptionResponse personality,
    List <RegionResponse> openToRegions,
    List<DayAvailabilityResponse> days,
    LfgIconOptionResponse timeOfDay,
    boolean saved,
    boolean flexible,
    LfgStatus lfgStatus,
    Instant createdAt,
    Instant expiresAt
) {

}
