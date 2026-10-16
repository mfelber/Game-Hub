package gamehub.game_Hub.Mapper;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.Response.LfgResponse;
import gamehub.game_Hub.Response.lfg.LfgIconOptionResponse;
import gamehub.game_Hub.Response.lfg.LfgOptionResponse;
import gamehub.game_Hub.enums.IconLfgOption;
import gamehub.game_Hub.enums.LfgOption;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LfgMapper {

  public <E extends Enum<E> & LfgOption> List<LfgOptionResponse> toOptions(Class<E> type) {
    return Arrays.stream(type.getEnumConstants())
        .map(e -> new LfgOptionResponse(e.name(), e.getLabel(), e.getDescription()))
        .toList();
  }

  public <E extends Enum<E> & IconLfgOption> List<LfgIconOptionResponse> toIconOptions(Class<E> type) {
    return Arrays.stream(type.getEnumConstants())
        .map(e -> new LfgIconOptionResponse(e.name(), e.getLabel(), e.getDescription(), e.getIcon()))
        .toList();
  }

  // public LfgResponse

}
