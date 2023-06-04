package cart.ui.advcie;

import cart.dto.ErrorResponse;
import cart.exception.AuthenticationException;
import cart.exception.CartItemException;
import cart.exception.MemberCouponException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handlerAuthenticationException(Exception exception) {
        ErrorType errorType = ErrorType.from(exception);
        HttpStatus httpStatus = errorType.getHttpStatus();
        ErrorResponse errorResponse = new ErrorResponse(errorType.name());

        return ResponseEntity.status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse);
    }
}
