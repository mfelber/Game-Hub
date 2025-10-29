package gamehub.game_Hub.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final RoleRepository roleRepository;

  private final PasswordEncoder passwordEncoder;

  private final PasswordResetTokenRepository passwordResetTokenRepository;

  private final UserRepository userRepository;

  private final EmailService emailService;

  private final AuthenticationManager authenticationManager;

  private final JwtService jwtService;

  @Value("${application.mailing.frontend.login-url}")
  private String logInUrl;

  public void registerUser(final RegistrationRequest request) throws MessagingException {
    var userRole = roleRepository.findByName("USER")
        .orElseThrow(() -> new IllegalStateException("Role USER was not initialized"));
    var user = User.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .username(request.getUsername())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .roles(List.of(userRole))
        .build();

    userRepository.save(user);
    sendWelcomeEmail(user);
  }

  public AuthenticationResponse authenticate(final @Valid AuthenticationRequest request) {
    final var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
        request.getEmail(), request.getPassword()
    ));
    var claims = new HashMap<String, Object>();
    var user = (User) authentication.getPrincipal();
    claims.put("fullName", user.getFullName());
    var jwtToken = jwtService.generateToken(claims, user);
    return AuthenticationResponse.builder().token(jwtToken).build();
  }

  public void forgotPassword(final ForgotPasswordRequest forgotPasswordRequest) throws MessagingException {
    User user = userRepository.findByEmail(forgotPasswordRequest.getEmail())
        .orElseThrow(() -> new IllegalStateException(
            "User with email " + forgotPasswordRequest.getEmail() + " not found"
        ));

    String resetLink = generateResetToken(user);
    sendResetPasswordEmail(user, resetLink);
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

  private void sendWelcomeEmail(final User user) throws MessagingException {
    emailService.sendWelcomeEmail(user.getEmail(),
        user.getName(),
        EmailTemplate.WELCOME_EMAIL,
        logInUrl, "Welcome to GameHub!");
  }

  private void sendResetPasswordEmail(final User user, String resetPasswordUrl) throws MessagingException {
    emailService.sendResetPasswordEmail(user.getEmail(), user.getName(), EmailTemplate.RESET_PASSWORD_MAIL,
        resetPasswordUrl, "Reset Password");
  }

  public void resetPassword(final PasswordResetToken token, final String newPassword) {
    User user = token.getUser();
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
    passwordResetTokenRepository.delete(token);
  }

}