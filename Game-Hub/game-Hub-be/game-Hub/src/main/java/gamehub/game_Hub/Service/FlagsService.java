package gamehub.game_Hub.Service;

import java.util.List;

import gamehub.game_Hub.Response.CommunityFlagsResponse;
import gamehub.game_Hub.Response.StoreFlagsResponse;

public interface FlagsService {

  List<StoreFlagsResponse> getStoreFlags();

  List<CommunityFlagsResponse> getCommunityFlags();

  List<CommunityFlagsResponse> getAllCommunityFlags();

}
