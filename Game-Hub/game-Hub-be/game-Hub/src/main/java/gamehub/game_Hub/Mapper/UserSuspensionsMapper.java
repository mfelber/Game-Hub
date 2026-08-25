package gamehub.game_Hub.Mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;


import org.springframework.stereotype.Service;

import gamehub.game_Hub.Module.UserSuspensions;
import gamehub.game_Hub.Repository.UserSuspensionRepository;
import gamehub.game_Hub.Response.Admin.AdminSuspendedAccountsResponse;

@Service
public class UserSuspensionsMapper {

  private final UserSuspensionRepository userSuspensionRepository;

  public UserSuspensionsMapper(final UserSuspensionRepository userSuspensionRepository) {
    this.userSuspensionRepository = userSuspensionRepository;
  }

  public AdminSuspendedAccountsResponse toUserSuspensionResponse(UserSuspensions userSuspension) {

    Long suspendedTimes = userSuspensionRepository.countByUserId(userSuspension.getUserId());

    Optional<UserSuspensions> previousSuspension  = userSuspensionRepository.findFirstByUserIdAndIdLessThanOrderByIdDesc(
        userSuspension.getUserId(), userSuspension.getId());

    LocalDate today = LocalDate.now();

    UserSuspensions lastSuspension = previousSuspension.orElse(userSuspension);

    LocalDate suspensionEnded = lastSuspension.getExpiresAt().toLocalDate();

    Long elapsedDays = ChronoUnit.DAYS.between(suspensionEnded, today);

    return AdminSuspendedAccountsResponse.builder()
        .suspensionId(userSuspension.getId())
        .reportId(userSuspension.getReport().getId())
        .userId(userSuspension.getUserId().getId())
        .userName(userSuspension.getUserId().getName())
        .suspensionReason(userSuspension.getSuspensionReason().getCommunityGuideline())
        .messageFromAdmin(userSuspension.getCustomMessage())
        .suspendedTimes(suspendedTimes)
        .daysSinceLastSuspension(elapsedDays)
        .expireAt(String.valueOf(userSuspension.getExpiresAt()))
        .suspensionStatus(userSuspension.getSuspensionStatus())
        .build();
  }

}
