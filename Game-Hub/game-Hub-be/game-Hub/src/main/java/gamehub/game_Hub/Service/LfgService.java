package gamehub.game_Hub.Service;

import java.util.List;

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

  List<LfgResponse> getMyActiveLfgPosts(Authentication connectedUser);

  Long createLfg(Authentication connectedUser, @Valid LfgCreationRequest request);

  LfgOptionsResponse getLfgOptions(Authentication connectedUser);

  Integer getMyActiveLfgPostsCount(Authentication connectedUser);

  Integer getSavedLfgPostsCount(Authentication connectedUser);

  Integer getMyLfgPostsLimit(Authentication connectedUser);

  SavedLfgPostId saveLfgPost(Long lfgPostId, Authentication connectedUser);

  void removeSavedLfgPost(Long lfgPostId, Authentication connectedUser);

}
