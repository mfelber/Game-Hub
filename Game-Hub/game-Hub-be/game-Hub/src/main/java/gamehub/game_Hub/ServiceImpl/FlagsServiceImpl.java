package gamehub.game_Hub.ServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.Module.Flags.FriendRequestOption;
import gamehub.game_Hub.Module.Flags.GroupInvitesOption;
import gamehub.game_Hub.Module.Flags.PlayTogetherInvitesOption;
import gamehub.game_Hub.Module.Flags.ProfileVisibilityOption;
import gamehub.game_Hub.Module.Flags.SendMessagesOption;
import gamehub.game_Hub.Repository.StoreFlagTypeRepository;
import gamehub.game_Hub.Response.CommunityFlagsResponse;
import gamehub.game_Hub.Response.StoreFlagsResponse;
import gamehub.game_Hub.Service.FlagsService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlagsServiceImpl implements FlagsService {

  private final StoreFlagTypeRepository storeFlagTypeRepository;

  @Override
  public List<StoreFlagsResponse> getStoreFlags() {
    return storeFlagTypeRepository.findAll()
        .stream()
        .map(flag -> new StoreFlagsResponse(flag.getFlagName(), flag.getDescription()))
        .toList();
  }

  @Override
  public List<CommunityFlagsResponse> getCommunityFlags() {
    List<CommunityFlagsResponse> flags = new ArrayList<>();

    // flags.add(new CommunityFlagsResponse("FRIEND_REQUEST",
    //     Arrays.stream(FriendRequestOption.values()).map(FriendRequestOption::getName).toList()));
    //
    // flags.add(new CommunityFlagsResponse("GROUP_INVITES",
    //     Arrays.stream(GroupInvitesOption.values()).map(GroupInvitesOption::getName).toList()));
    //
    // flags.add(new CommunityFlagsResponse("PLAY_TOGETHER",
    //     Arrays.stream(PlayTogetherInvitesOption.values()).map(PlayTogetherInvitesOption::getName).toList()));
    //
    // flags.add(new CommunityFlagsResponse("PROFILE_VISIBILITY",
    //     Arrays.stream(ProfileVisibilityOption.values()).map(ProfileVisibilityOption::getName).toList()));
    //
    // flags.add(new CommunityFlagsResponse("SEND_MESSAGES",
    //     Arrays.stream(SendMessagesOption.values()).map(SendMessagesOption::getName).toList()));

    return flags;
  }

  @Override
  public List<CommunityFlagsResponse> getAllCommunityFlags() {
    List<String> friendRequestOptions = Arrays.stream(FriendRequestOption.values())
        .map(FriendRequestOption::getName)
        .toList();
    List<String> sendMessageOptions = Arrays.stream(SendMessagesOption.values())
        .map(SendMessagesOption::getName)
        .toList();
    List<String> profileVisibilityOptions = Arrays.stream(ProfileVisibilityOption.values())
        .map(ProfileVisibilityOption::getName)
        .toList();
    List<String> groupInvitesOptions = Arrays.stream(GroupInvitesOption.values())
        .map(GroupInvitesOption::getName)
        .toList();
    List<String> playTogetherInvites = Arrays.stream(PlayTogetherInvitesOption.values())
        .map(PlayTogetherInvitesOption::getName)
        .toList();
    return List.of(new CommunityFlagsResponse("FRIEND_REQUEST", friendRequestOptions),
        new CommunityFlagsResponse("SEND_MESSAGES", sendMessageOptions),
        new CommunityFlagsResponse("PROFILE_VISIBILITY", profileVisibilityOptions),
        new CommunityFlagsResponse("GROUP_INVITES", groupInvitesOptions),
        new CommunityFlagsResponse("PLAY_TOGETHER", playTogetherInvites));
  }

}
