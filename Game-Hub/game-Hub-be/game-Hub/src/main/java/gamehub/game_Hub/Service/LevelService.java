package gamehub.game_Hub.Service;

import org.springframework.security.core.Authentication;

import gamehub.game_Hub.Response.LevelProgressResponse;

public interface LevelService {

  LevelProgressResponse getUserLevelProgress(Authentication connectedUser);

}
