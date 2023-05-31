package cart.exception;

import cart.cart_item.exception.CartItemException;
import cart.member.exception.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<Void> handlerAuthenticationException(AuthenticationException e) {
    infoLogging(e);

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  @ExceptionHandler(CartItemException.IllegalMember.class)
  public ResponseEntity<Void> handleException(CartItemException.IllegalMember e) {
    infoLogging(e);

    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ExceptionResponse> handleBusinessException(BusinessException exception) {
    infoLogging(exception);

    return new ResponseEntity<>(new ExceptionResponse(exception.getMessage()),
        exception.getHttpStatus());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExceptionResponse> handleUnexpectedException(Exception exception) {
    errorLogging(exception);

    return ResponseEntity.internalServerError()
        .body(new ExceptionResponse(String.format("전화 주세요 : %s", exception.getMessage())));
  }

  private void errorLogging(final Exception exception) {
    log.error("error stack = ", exception);
  }

  private void infoLogging(Exception exception) {
    log.info(
        "클래스 이름 = {} 메시지 = {}",
        exception.getClass().getSimpleName(),
        exception.getMessage()
    );
  }
}
