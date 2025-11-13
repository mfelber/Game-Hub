package gamehub.game_Hub.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import gamehub.game_Hub.Repository.CardColorRepository;
import gamehub.game_Hub.Response.CardColorResponse;
import gamehub.game_Hub.Service.ColorService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ColorServiceImpl implements ColorService {

  private final CardColorRepository cardColorRepository;

  @Override
  public List<CardColorResponse> findAllColors(final Authentication connectedUser) {
    return cardColorRepository.findAll()
        .stream()
        .map(cardColor -> new CardColorResponse(cardColor.getId(), cardColor.getColorName(), cardColor.getColorCode()))
        .collect(
            Collectors.toList());
  }

}
