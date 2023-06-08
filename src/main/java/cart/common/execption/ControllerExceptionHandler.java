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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleUnexpectedException(Exception e) {
        log.error("Unexpected Exception", e);
        return ResponseEntity.internalServerError().body(
                new ExceptionResponse(99999, "서버 내부에서 알 수 없는 예외가 발생했습니다.")
        );
    }
}
