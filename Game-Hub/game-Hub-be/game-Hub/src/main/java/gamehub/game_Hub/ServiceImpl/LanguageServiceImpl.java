package gamehub.game_Hub.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.Repository.LanguageRepository;
import gamehub.game_Hub.Response.LanguageResponse;
import gamehub.game_Hub.Service.LanguageService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {

  private final LanguageRepository languageRepository;

  @Override
  public List<LanguageResponse> findAllLanguages() {
    return languageRepository.findAll()
        .stream()
        .map(language -> new LanguageResponse(language.getId(), language.getName()))
        .toList();
  }

}
