package gamehub.game_Hub.ServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.Response.UnitSizeResponse;
import gamehub.game_Hub.Service.SystemRequirementsService;
import gamehub.game_Hub.enums.GameUnitSize;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SystemRequirementsServiceImpl implements SystemRequirementsService {

  @Override
  public List<UnitSizeResponse> findAllUnitSizes() {
    return Arrays.stream(GameUnitSize.values()).map(unitSize -> new UnitSizeResponse(unitSize.name())).collect(
        Collectors.toList());
  }

}
