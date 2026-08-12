package gamehub.game_Hub.Service;

import java.util.List;

import gamehub.game_Hub.Response.AgeRatingResponse;

public interface AgeRatingService {

  List<AgeRatingResponse> findAllAgeRating();

}
