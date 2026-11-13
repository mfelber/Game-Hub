package gamehub.game_Hub.Service;

import org.springframework.security.core.Authentication;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Module.embededIds.SavedLfgPostId;
import gamehub.game_Hub.Request.LfgCreationRequest;
import gamehub.game_Hub.Response.LfgResponse;
import gamehub.game_Hub.Response.lfg.LfgOptionsResponse;
import jakarta.validation.Valid;

public interface LfgService {

  PageResponse<LfgResponse> getAllActiveLfgPosts(Authentication connectedUser, String query , int page, int size);

  PageResponse<LfgResponse> getSavedLfgPosts(Authentication connectedUser, String query , int page, int size);

  Long createLfg(Authentication connectedUser, @Valid LfgCreationRequest request);

  LfgOptionsResponse getLfgOptions(Authentication connectedUser);

  Integer getCountOfActivePosts(Authentication connectedUser);

  Integer getSavedLfgPostsCount(Authentication connectedUser);

  Integer getMyLfgPostsLimit(Authentication connectedUser);

  SavedLfgPostId saveLfgPost(Long lfgPostId, Authentication connectedUser);

  void removeSavedLfgPost(Long lfgPostId, Authentication connectedUser);
}
