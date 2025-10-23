package gamehub.game_Hub.Authentication;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import gamehub.game_Hub.Module.User.PasswordResetToken;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Repository.user.PasswordResetTokenRepository;
import gamehub.game_Hub.Repository.user.UserRepository;
import gamehub.game_Hub.Service.AuthenticationService;
import gamehub.game_Hub.Service.TokenExpiredResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  private final UserRepository userRepository;

  private final PasswordResetTokenRepository passwordResetTokenRepository;

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public ResponseEntity<?> register(@RequestBody @Valid final RegistrationRequest request) throws MessagingException {
    authenticationService.registerUser(request);
    return ResponseEntity.accepted().build();
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid final AuthenticationRequest request) {
    return ResponseEntity.ok(authenticationService.authenticate(request));
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<String> processForgotPasswordRequest(
      @RequestBody @Valid final ForgotPasswordRequest forgotPasswordRequest)
      throws MessagingException {
    Optional<User> user = userRepository.findByEmail(forgotPasswordRequest.getEmail());
    if (user.isPresent()) {
      authenticationService.forgotPassword(forgotPasswordRequest);
    }
    return ResponseEntity.ok().build();
  }

  @PostMapping("/reset-password")
  public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam @Valid String newPassword) {
    PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);
    if (resetToken == null) {
      return ResponseEntity.badRequest().body("Invalid token");
    }

    if (resetToken.getExpirationDateTime().isBefore(LocalDateTime.now())) {
      return ResponseEntity.badRequest().body("Token has expired");
    }

    authenticationService.resetPassword(resetToken, newPassword);
    return ResponseEntity.ok().build();

  }

  @GetMapping("/reset-token-info")
  public ResponseEntity<TokenExpiredResponse> getResetTokenInfo(@RequestParam String token) {
    PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);

    boolean isExpired = resetToken.getExpirationDateTime().isBefore(LocalDateTime.now());
    return ResponseEntity.ok(new TokenExpiredResponse(isExpired));

  }

}
