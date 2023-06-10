package cart.common.execption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionResponse> handleBaseException(BaseException e) {
        BaseExceptionType baseExceptionType = e.exceptionType();
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                baseExceptionType.errorCode(), baseExceptionType.errorMessage()
        );
        log.warn("error = {}", exceptionResponse);
        return ResponseEntity.status(baseExceptionType.httpStatus())
                .body(exceptionResponse);
    }
}
