package gamehub.game_Hub.ServiceImpl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import gamehub.game_Hub.Mapper.BadgeMapper;
import gamehub.game_Hub.Module.Badge;
import gamehub.game_Hub.Module.BadgeCategory;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Repository.BadgeCategoryRepository;
import gamehub.game_Hub.Repository.badge.BadgeRepository;
import gamehub.game_Hub.Request.BadgeCategoryRequest;
import gamehub.game_Hub.Request.BadgeRequest;
import gamehub.game_Hub.Response.BadgeResponse;
import gamehub.game_Hub.Service.BadgeService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BadgeServiceImpl implements BadgeService {

  private final BadgeMapper badgeMapper;

  private final BadgeRepository badgeRepository;

  private final BadgeCategoryRepository badgeCategoryRepository;

  @Override
  public Long saveBadge(final BadgeRequest badgeRequest) {
    Badge badge = badgeMapper.toBadge(badgeRequest);
    return badgeRepository.save(badge).getId();
  }

  @Override
  public Long saveCategory(final BadgeCategoryRequest badgeCategoryRequest) {
    BadgeCategory badgeCategory = badgeMapper.toBadgeCategory(badgeCategoryRequest);
    return badgeCategoryRepository.save(badgeCategory).getId();
  }

  @Override
  public List<BadgeResponse> getAllUserBadges(final Authentication connectedUser) {
    User authUser = (User) connectedUser.getPrincipal();
      List<Badge> badges = badgeRepository.findByOwnedUsers((authUser));
    System.out.println(badges.stream().map(Badge::getName).collect(Collectors.toList()));
      List<BadgeResponse> responses = badges.stream().map(badgeMapper::toBadgeResponse).toList();;
      return responses;
  }

  // @Override
  // public List<BadgeResponse> getAllUserBadges(final Authentication connectedUser) {
  //   return List.of();
  // }

  // @Override
  // public List<BadgeResponse> getAllUserBadges(final Authentication connectedUser) {
  //   User authUser = (User) connectedUser.getPrincipal();
  //   List<Badge> badges = badgeRepository.findByUsersContaining(authUser);
  //   List<BadgeResponse> responses = badges.stream().map(badgeMapper::toBadgeResponse).toList();
  //   return responses;
  // }

}
