package gamehub.game_Hub.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import gamehub.game_Hub.Authentication.RegistrationRequest;
import gamehub.game_Hub.Email.EmailService;
import gamehub.game_Hub.Email.EmailTemplate;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Repository.role.RoleRepository;
import gamehub.game_Hub.Repository.user.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final EmailService emailService;

  @Value("${application.mailing.frontend.log-In-Url}")
  private String logInUrl;

  public void registerUser(final RegistrationRequest request) throws MessagingException {
    var userRole = roleRepository.findByName("USER").orElseThrow(() -> new IllegalStateException("Role USER was not initialized"));
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

  private void sendWelcomeEmail(final User user) throws MessagingException {
    emailService.sendEmail(user.getEmail(),
        user.getName(),
        EmailTemplate.WELCOME_EMAIL,
        logInUrl, "Welcome to GameHub!");
  }



}
