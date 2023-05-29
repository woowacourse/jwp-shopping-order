package cart.ui;

import static cart.exception.ErrorMessage.INTERNAL_SERVER_ERROR;

import cart.exception.CustomException;
import cart.exception.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.info("Custom Exception = ", e);
        return ResponseEntity.status(e.getStatus()).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception e) {
        log.warn("Unexpected Exception = ", e);
        return ResponseEntity.internalServerError().body(new ErrorResponse(INTERNAL_SERVER_ERROR.getMessage()));
    }
}
