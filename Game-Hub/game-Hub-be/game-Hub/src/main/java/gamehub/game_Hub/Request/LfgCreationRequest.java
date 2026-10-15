package gamehub.game_Hub.Request;

import java.time.DayOfWeek;
import java.util.Set;

import gamehub.game_Hub.enums.Lfg.Commitment;
import gamehub.game_Hub.enums.Languages;
import gamehub.game_Hub.enums.Lfg.GameType;
import gamehub.game_Hub.enums.Lfg.LookingFor;
import gamehub.game_Hub.enums.Lfg.LfgPostDuration;
import gamehub.game_Hub.enums.MicrophoneUsage;
import gamehub.game_Hub.enums.Lfg.Personality;
import gamehub.game_Hub.enums.Platform;
import gamehub.game_Hub.enums.Lfg.PlayStyle;
import gamehub.game_Hub.enums.Region;
import gamehub.game_Hub.enums.Lfg.TimeOfDay;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record LfgCreationRequest(

    @NotNull @Positive
    Long gameId,
    @NotEmpty @NotNull
    Set<Platform> platforms,
    @NotNull
    LookingFor lookingFor,
    PlayStyle playStyle,
    Commitment commitment,
    Set<GameType> gameTypes,
    @NotNull
    MicrophoneUsage voiceChat,
    Personality personality,
    @NotNull
    Set<DayOfWeek> days,
    @NotNull
    Set<TimeOfDay> timeOfDay,
    @NotNull
    Set<Region> openToRegion,
    @NotNull
    Set<Languages> preferredLanguages,
    boolean flexible,
    @Size(max = 300)
    String description,
    @NotNull
    LfgPostDuration lfgPostDuration
) {

}
