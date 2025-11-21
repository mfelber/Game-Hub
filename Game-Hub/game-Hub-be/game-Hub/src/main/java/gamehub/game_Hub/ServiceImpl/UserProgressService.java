package gamehub.game_Hub.ServiceImpl;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.Module.Badge;
import gamehub.game_Hub.Module.Level;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Repository.LevelRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserProgressService {

  private final LevelRepository levelRepository;

  public void awardBadgeAndXp(User user, Badge badge) {
    if (!user.getBadges().contains(badge)) {
      user.getBadges().add(badge);
    }

    addXpAndUpdateLevel(user,badge);
  }

  private void addXpAndUpdateLevel(final User user, final Badge badge) {
    Long xpReward = badge.getXpReward();
    Long userxp = user.getXp() + xpReward;

    user.setXp(userxp);

    Level currentLevel = user.getLevel();
    Level maxLevel = levelRepository.findTopByOrderByLevelNumberDesc();

    while (!currentLevel.getLevelNumber().equals(maxLevel.getLevelNumber())) {
      Level nextLevel = levelRepository.findByLevelNumber(currentLevel.getLevelNumber() + 1);

      if (nextLevel == null) {
        break;
      }

      if (user.getXp() < nextLevel.getRequiredXp()) {
        break;
      }

      user.setLevel(nextLevel);
      currentLevel = nextLevel;

    }

  }

}
