package cart.ui;

import cart.dto.response.ExceptionResponse;
import cart.exception.AuthenticationException;
import cart.exception.InvalidRequestValueException;
import cart.exception.NoSuchDataExistException;
import cart.exception.UnauthorizedAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class ControllerExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handlerAuthenticationException(final AuthenticationException e) {
        log.warn("인증에 실패하였습니다.\n{}", e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ExceptionResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ExceptionResponse> handleException(final UnauthorizedAccessException e) {
        log.warn("해당 요청에 대한 권한이 없습니다.\n{}", e.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ExceptionResponse(HttpStatus.FORBIDDEN.value(), e.getMessage()));
    }

    @ExceptionHandler(NoSuchDataExistException.class)
    public ResponseEntity<ExceptionResponse> handleNoSuchDataExistException(final NoSuchDataExistException e) {
        log.warn("존재하지 않는 자원에 접근하였습니다.\n[{}] : {}", e.getData(), e.getDataId());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), "존재하지 않는 자원 접근입니다."));
    }

    @ExceptionHandler(InvalidRequestValueException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidRequestValueException(final InvalidRequestValueException e) {
        log.warn("비즈니스 정책에 유효하지 않은 요청입니다.\n{}", e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleMethodArgumentTypeMismatchException(
            final MethodArgumentTypeMismatchException e) {
        final Throwable mostSpecificCause = e.getMostSpecificCause();

        log.warn("잘못된 요청입니다.\n{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), mostSpecificCause.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleRuntimeException(final Exception e) {
        final String unexpectedErrorMessage = "예기치 못한 오류가 발생했습니다.";
        log.error(unexpectedErrorMessage, e);
        
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), unexpectedErrorMessage));
    }
}
