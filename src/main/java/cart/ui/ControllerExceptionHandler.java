package cart.ui;

import static java.util.stream.Collectors.joining;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import cart.dto.response.ErrorResponse;
import cart.exception.ApplicationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handlerAuthenticationException(ApplicationException exception) {
        log(exception);
        return ResponseEntity.status(exception.status()).body(new ErrorResponse(exception));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        String exceptionMessage = exception.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(joining(System.lineSeparator()));
        return ResponseEntity.status(UNPROCESSABLE_ENTITY)
                .body(new ErrorResponse(UNPROCESSABLE_ENTITY, exceptionMessage));
    }

    private void log(ApplicationException e) {
        logger.error(e.toString());
        logger.error(e.getStackTrace());
    }
}
