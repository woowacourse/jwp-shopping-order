package cart.ui;

import cart.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle(Exception e) {
        LOGGER.warn(e.getMessage());
        return ResponseEntity.internalServerError().body("알 수 없는 서버 에러가 발생했습니다.");
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handle(CustomException e) {
        LOGGER.info(e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }
}
