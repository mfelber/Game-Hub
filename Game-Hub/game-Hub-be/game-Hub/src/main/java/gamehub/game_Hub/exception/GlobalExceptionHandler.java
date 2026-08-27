package gamehub.game_Hub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AccountSuspendedException.class)
  public ResponseEntity<ErrorResponse> handleAccountSuspended(AccountSuspendedException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("ACCOUNT_SUSPENDED", e.getMessage()));
  }

  @ExceptionHandler(AccountBannedException.class)
  public ResponseEntity<ErrorResponse> handleAccountBanned(AccountBannedException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("ACCOUNT_BANNED",e.getMessage()));
  }

  @ExceptionHandler(InvalidCredentials.class)
  public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentials e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("INVALID_CREDENTIALS",e.getMessage()));
  }



}
