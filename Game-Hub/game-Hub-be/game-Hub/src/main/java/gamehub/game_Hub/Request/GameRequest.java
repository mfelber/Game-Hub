package gamehub.game_Hub.Request;

import java.util.Set;

import gamehub.game_Hub.Module.Genre;
import gamehub.game_Hub.Module.Language;
import gamehub.game_Hub.Module.Platform;
import gamehub.game_Hub.Module.Subtitles;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record GameRequest(
    Long gameId,
    @NotNull(message = "100")
    @NotEmpty(message = "100")
    String title,
    @NotEmpty(message = "101")
    Set<Genre> genres,
    @NotNull(message = "102")
    @NotEmpty(message = "102")
    String description,
    @NotNull(message = "103")
    @NotEmpty(message = "103")
    String publisher,
    @NotNull(message = "104")
    @NotEmpty(message = "104")
    String developer,
    @NotNull(message = "105")
    @NotEmpty(message = "105")
    String releaseYear,
    @NotNull(message = "106")
    Double price,
    @NotNull(message = "107")
    String cpu,
    @NotNull(message = "108")
    String gpu,
    @NotNull(message = "109")
    String ram,
    @NotNull(message = "110")
    String storage,
    @NotNull(message = "111")
    String os,
    @NotNull(message = "112")
    Set<Platform> platforms,
    @NotNull(message = "113")
    Set<Language> languages,
    @NotNull(message = "114")
    Set<Subtitles> subtitles
) {

}
