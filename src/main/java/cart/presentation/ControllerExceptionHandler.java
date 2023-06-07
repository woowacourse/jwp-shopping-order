package cart.presentation;

import cart.exception.ExpectedException;
import cart.exception.application.ApplicationException;
import cart.exception.presentation.AuthorizationHeaderNotValidException;
import cart.presentation.dto.response.ExceptionResponse;
import com.fasterxml.jackson.databind.DatabindException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.BindException;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler({
            ExpectedException.class,
            DatabindException.class
    })
    public ResponseEntity<ExceptionResponse> handleExpectedException(ExpectedException e) {
        logger.debug(e.getMessage());
        HttpStatus status = ExceptionStatusMapper.of(e);
        return ResponseEntity.status(status)
                .body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return ResponseEntity.internalServerError()
                .body(new ExceptionResponse("예기치 못한 오류가 발생했습니다."));
    }
}
