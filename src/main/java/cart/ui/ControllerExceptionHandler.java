package cart.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import cart.dto.ErrorResponse;
import cart.exception.ApplicationException;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnhandled(Exception e) {
        log(e);
        return ResponseEntity.internalServerError().build();
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handlerAuthenticationException(ApplicationException e) {
        log(e);
        return ResponseEntity.status(e.status()).body(new ErrorResponse(e));
    }

    private void log(Exception e) {
        logger.error(e.toString());
        logger.error(e.getStackTrace());
        e.printStackTrace();
    }
}
