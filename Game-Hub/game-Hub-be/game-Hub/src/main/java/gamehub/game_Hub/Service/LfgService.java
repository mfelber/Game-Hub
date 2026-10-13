package gamehub.game_Hub.Service;

import org.springframework.security.core.Authentication;

import gamehub.game_Hub.Request.LfgCreationRequest;
import jakarta.validation.Valid;

public interface LfgService {

  Long createLfg(Authentication connectedUser, @Valid LfgCreationRequest request);

}
