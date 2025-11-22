package gamehub.game_Hub.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.Repository.StoreFlagTypeRepository;
import gamehub.game_Hub.Response.StoreFlagsResponse;
import gamehub.game_Hub.Service.StoreFlagsService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreFlagsServiceImpl implements StoreFlagsService {

  private final StoreFlagTypeRepository storeFlagTypeRepository;

  @Override
  public List<StoreFlagsResponse> getAllFlags() {
    return storeFlagTypeRepository.findAll().stream().map(flag -> new StoreFlagsResponse(flag.getFlagName(),flag.getDescription())).toList();
  }

}
