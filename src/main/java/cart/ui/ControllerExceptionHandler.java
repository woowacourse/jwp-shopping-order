package cart.ui;

import cart.dto.response.ErrorResponse;
import cart.dto.response.Response;
import cart.exception.AuthenticationException;
import cart.exception.CartItemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
    Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handlerAuthenticationException(AuthenticationException e) {
        logger.error("인증 예외 발생: " + e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(CartItemException.IllegalMember.class)
    public ResponseEntity<Void> handleException(CartItemException.IllegalMember e) {
        logger.error("장바구니 예외 발생: " + e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Void> handleException(RuntimeException e) {
        logger.error("알 수 없는 예외 발생: " + e, e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handle(MethodArgumentNotValidException e) {
        logger.error("잘못된 요청: " + e, e);
        ErrorResponse errorResponse = ErrorResponse.badRequest("잘못된 요청입니다.");
        for (FieldError fieldError : e.getFieldErrors()) {
            errorResponse.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity
                .badRequest()
                .body(errorResponse);
    }
}
