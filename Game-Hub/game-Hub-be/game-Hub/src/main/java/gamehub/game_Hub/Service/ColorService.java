package gamehub.game_Hub.Service;

import java.util.List;

import org.springframework.security.core.Authentication;

import gamehub.game_Hub.Response.CardColorResponse;

public interface ColorService {

  List<CardColorResponse> findAllColors(Authentication connectedUser);

}
