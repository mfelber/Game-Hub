package gamehub.game_Hub.Service;

import java.util.List;

import org.springframework.security.core.Authentication;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Request.LfgCreationRequest;
import gamehub.game_Hub.Response.LfgResponse;
import gamehub.game_Hub.Response.lfg.LfgOptionsResponse;
import gamehub.game_Hub.enums.Lfg.LookingFor;
import jakarta.validation.Valid;

public interface LfgService {

  Long createLfg(Authentication connectedUser, @Valid LfgCreationRequest request);

  LfgOptionsResponse getLfgOptions(Authentication connectedUser);

  PageResponse<LfgResponse> getAllActiveLfgs(Authentication connectedUser, int page, int size);

}
