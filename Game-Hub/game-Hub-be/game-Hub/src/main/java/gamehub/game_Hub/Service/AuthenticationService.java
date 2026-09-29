package gamehub.game_Hub.Service;

import static gamehub.game_Hub.enums.AccountType.ADULT;
import static gamehub.game_Hub.enums.AccountType.CHILD;
import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gamehub.game_Hub.Email.SendEmailUserService;
import gamehub.game_Hub.Module.CardColor;
import gamehub.game_Hub.Module.Level;
import gamehub.game_Hub.Module.userSettings.Access;
import gamehub.game_Hub.Module.userSettings.CommunitySettings;
import gamehub.game_Hub.Module.userSettings.PrivacySettings;
import gamehub.game_Hub.Module.userSettings.StoreSettings;
import gamehub.game_Hub.Module.userSettings.UserSettings;
import gamehub.game_Hub.Repository.CommunitySettingsDefinitionRepository;
import gamehub.game_Hub.Repository.PegiRatingRepository;
import gamehub.game_Hub.Repository.PrivacySettingsDefinitionRepository;
import gamehub.game_Hub.Repository.PrivacySettingsRepository;
import gamehub.game_Hub.Repository.UserCommunitySettingsRepository;
import gamehub.game_Hub.Repository.UserSettingsRepository;
import gamehub.game_Hub.Repository.UserStoreSettingsRepository;
import gamehub.game_Hub.enums.AccountStatus;
import gamehub.game_Hub.enums.Country;
import gamehub.game_Hub.enums.Role;
import gamehub.game_Hub.enums.Status;
import gamehub.game_Hub.Repository.CardColorRepository;
import gamehub.game_Hub.Repository.LevelRepository;
import gamehub.game_Hub.Request.AuthenticationRequest;
import gamehub.game_Hub.Response.AuthenticationResponse;
import gamehub.game_Hub.Request.ForgotPasswordRequest;
import gamehub.game_Hub.Request.RegistrationRequest;
import gamehub.game_Hub.Module.User.PasswordResetToken;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Repository.role.RoleRepository;
import gamehub.game_Hub.Repository.user.PasswordResetTokenRepository;
import gamehub.game_Hub.Repository.user.UserRepository;
import gamehub.game_Hub.Security.JwtService;
import gamehub.game_Hub.exception.AccountBannedException;
import gamehub.game_Hub.exception.AccountSuspendedException;
import gamehub.game_Hub.exception.InvalidCredentials;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final RoleRepository roleRepository;

  private final PasswordEncoder passwordEncoder;

  private final PasswordResetTokenRepository passwordResetTokenRepository;

  private final UserRepository userRepository;

  private final SendEmailUserService emailUserService;

  private final AuthenticationManager authenticationManager;

  private final JwtService jwtService;

  private final CardColorRepository cardColorRepository;

  private final LevelRepository levelRepository;

  private final UserStoreSettingsRepository userStoreSettingsRepository;

  private final PegiRatingRepository pegiRatingRepository;

  private final CommunitySettingsDefinitionRepository communitySettingsDefinitionRepository;

  private final UserCommunitySettingsRepository userCommunitySettingsRepository;

  private final UserSettingsRepository userSettingsRepository;

  private final PrivacySettingsDefinitionRepository privacySettingsDefinitionRepository;

  private final PrivacySettingsRepository privacySettingsRepository;

  @Value("${application.mailing.frontend.login-url}")
  private String logInUrl;

  public void registerUser(final RegistrationRequest request) throws MessagingException {
    var userRole = roleRepository.findByName("USER")
        .orElseThrow(() -> new IllegalStateException("Role USER was not initialized"));

    CardColor defaultColor = cardColorRepository.findById(1L)
        .orElseThrow(() -> new EntityNotFoundException("Card Color was not initialized"));

    Level defaultLevel = levelRepository.findById(1L)
        .orElseThrow(() -> new EntityNotFoundException("Level was not initialized"));

    if (request.isChildAccount()) {
      registerChildUser(request);
    } else {
      var user = User.builder()
          .firstName(request.getFirstName())
          .lastName(request.getLastName())
          .username(request.getUsername())
          .email(request.getEmail())
          .password(passwordEncoder.encode(request.getPassword()))
          .role(Role.USER)
          .status(Status.OFFLINE)
          .accountStatus(AccountStatus.ACTIVE)
          .country(Country.NOT_SPECIFIED)
          .profileColor(getRandomColor())
          .bannerType("PREDEFINED")
          .banner("/assets/banners/banner_1.jpg")
          .cardColor(defaultColor)
          .xp(0L)
          .level(defaultLevel)
          .accountType(ADULT)
          .build();

      userRepository.save(user);
      var userSettings = UserSettings.builder()
          .user(user)
          .build();

      userSettingsRepository.save(userSettings);
      setAdultAccountSettings(user);
      emailUserService.sendWelcomeEmail(user);
    }
  }

  @Transactional
  public void registerChildUser(final @Valid RegistrationRequest request) throws MessagingException {
    var UserRole = roleRepository.findByName("USER")
        .orElseThrow(() -> new IllegalStateException("Role USER was not initialized"));

    CardColor defaultColor = cardColorRepository.findById(1l)
        .orElseThrow(() -> new EntityNotFoundException("Card Color was not initialized"));

    Level defaultLevel = levelRepository.findById(1L)
        .orElseThrow(() -> new EntityNotFoundException("Level was not initialized"));

    var user = User.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .username(request.getUsername())
        .email(request.getEmail())
        .parentEmail(request.getParentEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .status(Status.OFFLINE)
        .accountStatus(AccountStatus.ACTIVE)
        .country(Country.NOT_SPECIFIED)
        .profileColor(getRandomColor())
        .bannerType("PREDEFINED")
        .banner("/assets/banners/banner_1.jpg")
        .cardColor(defaultColor)
        .xp(0L)
        .level(defaultLevel)
        .accountType(CHILD)
        .build();

    userRepository.save(user);
    var userSettings = UserSettings.builder()
        .user(user)
        .build();

    userSettingsRepository.save(userSettings);
    createChildAccountSettings(user);
    emailUserService.sendWelcomeEmail(user);
  }

  private void createChildAccountSettings(final User user) {

    Map<String, Access> childCommunitySettings = Map.of(
        "Friend Requests", Access.EVERYONE,
        "Group invites", Access.FRIENDS,
        "Group event invites", Access.FRIENDS,
        "Play together invites", Access.FRIENDS,
        "Send messages", Access.FRIENDS
        );

    Map<String, Access> childPrivacySettings = Map.of(
        "Profile visibility", Access.FRIENDS,
        "Wishlist visibility", Access.FRIENDS,
        "Friends list visibility", Access.FRIENDS,
        "Groups visibility", Access.FRIENDS,
        "Game activity visibility", Access.FRIENDS,
        "Favorite game visibility", Access.FRIENDS
    );

    var pegiRatings = pegiRatingRepository.findAll();

    var storeSettings = pegiRatings.stream()
        .map(pegiRating -> StoreSettings.builder()
            .user(user)
            .pegiRating(pegiRating)
            .disabled(!Set.of("PEGI 3", "PEGI 7").contains(pegiRating.getName()))
            .build()).toList();

    var communitySettingDefinitions =
        communitySettingsDefinitionRepository.findAll();

    var communitySettings = communitySettingDefinitions.stream()
        .map(definition -> CommunitySettings.builder()
            .user(user)
            .settingDefinition(definition)
            .access(childCommunitySettings.get(definition.getName()))
            .build())
        .toList();

    var privacySettingDefinitions = privacySettingsDefinitionRepository.findAll();

    var privacySettings = privacySettingDefinitions.stream()
        .map(definition -> PrivacySettings.builder()
            .user(user)
            .settingDefinition(definition)
            .access(childPrivacySettings.get(definition.getName()))
            .build())
        .toList();


    privacySettingsRepository.saveAll(privacySettings);
    userCommunitySettingsRepository.saveAll(communitySettings);
    userStoreSettingsRepository.saveAll(storeSettings);
  }

  private void setAdultAccountSettings(final User user) {

    Map<String, Access> adultCommunitySettings = Map.of(
        "Friend Requests", Access.EVERYONE,
        "Group invites", Access.EVERYONE,
        "Group event invites", Access.EVERYONE,
        "Play together invites", Access.FRIENDS,
        "Profile visibility", Access.EVERYONE,
        "Send messages", Access.FRIENDS
    );

    Map<String, Access> adultPrivacySettings = Map.of(
        "Profile visibility", Access.EVERYONE,
        "Wishlist visibility", Access.EVERYONE,
        "Friends list visibility", Access.EVERYONE,
        "Groups visibility", Access.EVERYONE,
        "Game activity visibility", Access.EVERYONE,
        "Favorite game visibility", Access.EVERYONE
    );

    var pegiRatings = pegiRatingRepository.findAll();

    var storeSettings = pegiRatings.stream()
        .map(pegiRating -> StoreSettings.builder()
            .user(user)
            .pegiRating(pegiRating)
            .disabled(false)
            .build()).toList();

    var communitySettingDefinitions =
        communitySettingsDefinitionRepository.findAll();

    var communitySettings = communitySettingDefinitions.stream()
        .map(definition -> CommunitySettings.builder()
            .user(user)
            .settingDefinition(definition)
            .access(adultCommunitySettings.get(definition.getName()))
          .build())
      .toList();

    var privacySettingDefinitions = privacySettingsDefinitionRepository.findAll();

    var privacySettings = privacySettingDefinitions.stream()
        .map(definition -> PrivacySettings.builder()
            .user(user)
            .settingDefinition(definition)
            .access(adultPrivacySettings.get(definition.getName()))
            .build())
        .toList();

    privacySettingsRepository.saveAll(privacySettings);
    userCommunitySettingsRepository.saveAll(communitySettings);
    userStoreSettingsRepository.saveAll(storeSettings);
  }

  public String getRandomColor() {

    final Random random = new Random();
    final String[] letters = "0123456789ABCDEF".split("");
    String color = "#";
    for (int i = 0; i < 6; i++) {
      color += letters[Math.round(random.nextFloat() * 15)];
    }
    return color;
  }

  public AuthenticationResponse authenticate(final @Valid AuthenticationRequest request) {
    Authentication authentication;

    try {
      authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
          request.getEmail(), request.getPassword()
      ));
    } catch (BadCredentialsException e) {
      throw new InvalidCredentials("Invalid email or password");
    }

    var claims = new HashMap<String, Object>();
    var user = (User) authentication.getPrincipal();

    canUserLogIn(user);

    claims.put("fullName", user.getFullName());
    var jwtToken = jwtService.generateToken(claims, user);
    return AuthenticationResponse.builder().token(jwtToken).role(user.getRole().name()).build();
  }

  public void forgotPassword(final ForgotPasswordRequest forgotPasswordRequest) throws MessagingException {
    User user = userRepository.findByEmail(forgotPasswordRequest.getEmail())
        .orElseThrow(() -> new IllegalStateException(
            "User with email " + forgotPasswordRequest.getEmail() + " not found"
        ));

    String resetLink = generateResetToken(user);
    emailUserService.sendResetPasswordEmail(user, resetLink);
  }

  public String generateResetToken(User user) {
    PasswordResetToken existingToken = passwordResetTokenRepository.findByUser(user);
    if (existingToken != null) {
      passwordResetTokenRepository.delete(existingToken);
    }
    UUID uuid = UUID.randomUUID();
    LocalDateTime currentDateTime = LocalDateTime.now();
    LocalDateTime expirationDateTime = currentDateTime.plusMinutes(30);

    PasswordResetToken resetToken = PasswordResetToken.builder()
        .token(uuid.toString())
        .user(user)
        .expirationDateTime(expirationDateTime)
        .build();

    passwordResetTokenRepository.save(resetToken);

    return "http://localhost:4200/reset-password?token=" + uuid;

  }

  private void canUserLogIn(User user) {
    if (user.getAccountStatus() == AccountStatus.SUSPENDED) {
      throw new AccountSuspendedException("Your account is currently suspended");
    }

    if (user.getAccountStatus() == AccountStatus.BANNED) {
      throw new AccountBannedException("Your account is permanently banned");
    }
  }

  public void resetPassword(final PasswordResetToken token, final String newPassword) {
    User user = token.getUser();
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
    passwordResetTokenRepository.delete(token);
  }

}