package gamehub.game_Hub.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gamehub.game_Hub.Response.CardColorResponse;
import gamehub.game_Hub.Service.ColorService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("color")
@RequiredArgsConstructor
public class CardColorController {

  private final ColorService colorService;

  // Retrieve all predefined colors for customizing user profile cards
  @GetMapping("/all-colors")
  public ResponseEntity<List<CardColorResponse>> getColors(Authentication connectedUser){
    return ResponseEntity.ok(colorService.findAllColors(connectedUser));
  }

}
