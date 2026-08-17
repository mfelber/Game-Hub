package gamehub.game_Hub.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gamehub.game_Hub.Response.UnitSizeResponse;
import gamehub.game_Hub.Service.SystemRequirementsService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/system-requirements")
@RequiredArgsConstructor
public class SystemRequirementsController {

  private final SystemRequirementsService systemRequirementsService;

  @GetMapping("/unit-sizes")
  public ResponseEntity<List<UnitSizeResponse>> getUnitSizes() {
    return ResponseEntity.ok(systemRequirementsService.findAllUnitSizes());
  }
}
