package gamehub.game_Hub.specification;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.enums.AccountStatus;
import gamehub.game_Hub.enums.Country;
import gamehub.game_Hub.enums.MicrophoneUsage;
import gamehub.game_Hub.enums.Platform;
import gamehub.game_Hub.enums.Region;
import gamehub.game_Hub.enums.Role;
import gamehub.game_Hub.enums.Status;

public class UserSpecification {

  public static Specification<User> emailIsNot(String email) {
    return (root, query, cb) -> cb.notEqual(root.get("email"), email);
  }

  public static Specification<User> hasRole(Role role) {
    return (root, query, cb) -> cb.equal(root.get("role"), role);
  }

  public static Specification<User> accountStatusIn(List<AccountStatus> statuses) {
    return (root, query, cb) -> root.get("accountStatus").in(statuses);
  }

  public static Specification<User> usernameContains(String searchQuery) {
    return (root, query2, cb) -> cb.like(cb.lower(root.get("username")), "%" + searchQuery.toLowerCase() + "%");
  }

  public static Specification<User> countryIn(List<Country> countries) {
    return (root, query, cb) -> root.get("country").in(countries);
  }

  public static Specification<User> regionIn(List<Region> regions) {
    return (root, query, cb) -> root.get("region").in(regions);
  }

  public static Specification<User> platformIn(List<Platform> platforms) {
    return (root, query, cb) -> root.get("mainPlatform").in(platforms);
  }

  public static Specification<User> microphoneIn(List<MicrophoneUsage> micUsage) {
    return (root, query, cb) -> root.get("microphoneUsage").in(micUsage);
  }

  public static Specification<User> isOnline() {
    return (root, query, cb) -> root.get("status").in(Status.ONLINE);
  }

}
