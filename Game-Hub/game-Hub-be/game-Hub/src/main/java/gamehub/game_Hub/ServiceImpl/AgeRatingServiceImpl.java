package gamehub.game_Hub.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.Repository.AgeRatingRepository;
import gamehub.game_Hub.Response.AgeRatingResponse;
import gamehub.game_Hub.Service.AgeRatingService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AgeRatingServiceImpl implements AgeRatingService {

  private final AgeRatingRepository ageRatingRepository;

  @Override
  public List<AgeRatingResponse> findAllAgeRating() {
    return ageRatingRepository.findAll()
        .stream()
        .map(ageRating -> new AgeRatingResponse(ageRating.getId(), ageRating.getAgeRating(),
            ageRating.getAgeRatingColor()))
        .toList();
  }

}
