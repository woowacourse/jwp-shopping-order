package cart.exception;

import static cart.exception.ErrorCode.FORBIDDEN;
import static cart.exception.ErrorCode.INTERNAL_SERVER_ERROR;
import static cart.exception.ErrorCode.INVALID_REQUEST;
import static cart.exception.ErrorCode.UNAUTHORIZED;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponses> methodArgumentNotValidException(final MethodArgumentNotValidException e) {
        final List<String> errorMessage = getErrorMessage(e);
        log.error(String.join("", errorMessage), e);
        final ErrorResponses errorResponse = new ErrorResponses(INVALID_REQUEST, errorMessage);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> authenticationException(final AuthenticationException e) {
        log.error(UNAUTHORIZED.getMessage(), e);
        final ErrorResponse errorResponse = new ErrorResponse(UNAUTHORIZED, UNAUTHORIZED.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> forbiddenException(final ForbiddenException e) {
        log.error(FORBIDDEN.getMessage(), e);
        final ErrorResponse errorResponse = new ErrorResponse(FORBIDDEN, FORBIDDEN.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundException(final NotFoundException e) {
        log.error(e.getErrorCode().getMessage(), e);
        final ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode(), e.getErrorCode().getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> badRequestException(final BadRequestException e) {
        log.error(e.getErrorCode().getMessage(), e);
        final ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode(), e.getErrorCode().getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DBException.class)
    public ResponseEntity<ErrorResponse> dbException(final DBException e) {
        log.error(e.getErrorCode().getMessage(), e);
        final ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode(), e.getErrorCode().getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(final Exception e) {
        log.error(INTERNAL_SERVER_ERROR.getMessage(), e);
        final ErrorResponse errorResponse = new ErrorResponse(INTERNAL_SERVER_ERROR,
            INTERNAL_SERVER_ERROR.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    private List<String> getErrorMessage(final MethodArgumentNotValidException e) {
        return e.getFieldErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.toList());
    }
}
