package gamehub.game_Hub.Service;

import static gamehub.game_Hub.enums.AccountType.ADULT;
import static gamehub.game_Hub.enums.AccountType.CHILD;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import gamehub.game_Hub.Email.SendEmailUserService;
import gamehub.game_Hub.Module.CardColor;
import gamehub.game_Hub.Module.Flags.CommunityFlagType;
import gamehub.game_Hub.Module.Flags.StoreFlagType;
import gamehub.game_Hub.Module.Flags.UserCommunityFlag;
import gamehub.game_Hub.Module.Flags.UserStoreFlag;
import gamehub.game_Hub.Module.Level;
import gamehub.game_Hub.enums.AccountStatus;
import gamehub.game_Hub.enums.Location;
import gamehub.game_Hub.enums.Role;
import gamehub.game_Hub.enums.Status;
import gamehub.game_Hub.Repository.CardColorRepository;
import gamehub.game_Hub.Repository.CommunityFlagTypeRepository;
import gamehub.game_Hub.Repository.LevelRepository;
import gamehub.game_Hub.Repository.StoreFlagTypeRepository;
import gamehub.game_Hub.Repository.UserCommunityFlagRepository;
import gamehub.game_Hub.Repository.UserStoreFlagRepository;
import gamehub.game_Hub.Request.AuthenticationRequest;
import gamehub.game_Hub.Response.AuthenticationResponse;
import gamehub.game_Hub.Request.ForgotPasswordRequest;
import gamehub.game_Hub.Request.RegistrationRequest;
import gamehub.game_Hub.Email.EmailService;
import gamehub.game_Hub.Email.EmailTemplate;
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

  private final StoreFlagTypeRepository storeFlagTypeRepository;

  private final UserStoreFlagRepository userStoreFlagRepository;

  private final CommunityFlagTypeRepository communityFlagTypeRepository;

  private final UserCommunityFlagRepository userCommunityFlagRepository;

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
          .location(Location.UNKNOWN)
          .profileColor(getRandomColor())
          .bannerType("PREDEFINED")
          .banner("/assets/banners/banner_1.jpg")
          .cardColor(defaultColor)
          .xp(0L)
          .level(defaultLevel)
          .accountType(ADULT)
          .build();

      userRepository.save(user);
      setAdultAccountFlags(user);
      emailUserService.sendWelcomeEmail(user);
    }
  }

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
        .location(Location.UNKNOWN)
        .profileColor(getRandomColor())
        .bannerType("PREDEFINED")
        .banner("/assets/banners/banner_1.jpg")
        .cardColor(defaultColor)
        .xp(0L)
        .level(defaultLevel)
        .accountType(CHILD)
        .build();

    userRepository.save(user);
    setChildAccountFlags(user);
    emailUserService.sendWelcomeEmail(user);
  }

  private void setChildAccountFlags(final User user) {
    for (StoreFlagType flagType : storeFlagTypeRepository.findAll()) {
      UserStoreFlag storeFlag = new UserStoreFlag();
      storeFlag.setUser(user);
      storeFlag.setUserFlagType(flagType);
      storeFlag.setValue(true);
      userStoreFlagRepository.save(storeFlag);
    }

    for (CommunityFlagType flagType : communityFlagTypeRepository.findAll()) {
      UserCommunityFlag communityFlag = new UserCommunityFlag();
      communityFlag.setUser(user);
      communityFlag.setUserFlagType(flagType);
      switch (flagType.getFlagCode()) {
        case "FRIEND_REQUEST", "GROUP_INVITES", "PLAY_TOGETHER_INVITES", "PROFILE_VISIBILITY", "SEND_MESSAGES":
          communityFlag.setValue("No one");
          break;
        default:
          throw new IllegalArgumentException("Unknown flag code: " + flagType.getFlagCode());
      }
      userCommunityFlagRepository.save(communityFlag);
    }
  }

  private void setAdultAccountFlags(final User user) {
    for (StoreFlagType flagType : storeFlagTypeRepository.findAll()) {
      UserStoreFlag flag = new UserStoreFlag();
      flag.setUser(user);
      flag.setUserFlagType(flagType);
      flag.setValue(false);
      userStoreFlagRepository.save(flag);
    }

    for (CommunityFlagType flagType : communityFlagTypeRepository.findAll()) {
      UserCommunityFlag comflag = new UserCommunityFlag();
      comflag.setUser(user);
      comflag.setUserFlagType(flagType);
      switch (flagType.getFlagCode()) {
        case "FRIEND_REQUEST":
          comflag.setValue("Every one");
          break;
        case "GROUP_INVITES", "PLAY_TOGETHER_INVITES", "PROFILE_VISIBILITY", "SEND_MESSAGES":
          comflag.setValue("Friends");
          break;
        default:
          throw new IllegalArgumentException("Unknown flag code: " + flagType.getFlagCode());
      }
      userCommunityFlagRepository.save(comflag);
    }

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
    if (user.getAccountStatus() == AccountStatus.SUSPENDED){
      throw new AccountSuspendedException("Your account is currently suspended");
    }

    if (user.getAccountStatus() == AccountStatus.BANNED){
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