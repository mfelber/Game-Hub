package gamehub.game_Hub.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.Repository.PlatformRepository;
import gamehub.game_Hub.Response.OperationSystemResponse;
import gamehub.game_Hub.Service.PlatformService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlatformServiceImpl implements PlatformService {

  private final PlatformRepository platformRepository;

  @Override
  public List<OperationSystemResponse> findAllPlatforms() {
    return platformRepository.findAll()
        .stream()
        .map(platform -> new OperationSystemResponse(platform.getId(), platform.getName()))
        .toList();
  }

}
