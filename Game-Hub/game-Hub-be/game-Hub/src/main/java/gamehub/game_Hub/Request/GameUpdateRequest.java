package gamehub.game_Hub.Request;

import java.util.Set;

import io.micrometer.common.lang.Nullable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record GameUpdateRequest (

  @NotNull @NotEmpty
  String title,
  @NotNull @NotEmpty
  String description,
  @NotNull @NotEmpty
  String publisher,
  @NotNull @NotEmpty
  String developer,
  @NotNull @NotEmpty
  String releaseYear,
  @NotNull
  Double price,
  @Schema(nullable = true)
  Integer discountPercent,
  @NotNull
  String cpu,
  @NotNull
  String gpu,
  @NotNull
  Integer ram,
  @NotNull
  Integer storage,
  @NotNull
  String gameUnitSize,
  @NotNull
  Set<Long> platformIds,
  @NotNull
  Set<Long> languageIds,
  @NotNull
  Set<Long> subtitleIds,
  @NotEmpty
  Set<Long> genresIds,
  @NotNull
  Long ageRatingId
) {}


