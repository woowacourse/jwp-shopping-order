package cart.ui.common;

import cart.exception.authentication.AuthenticationException;
import cart.exception.authorization.AuthorizationException;
import cart.exception.business.BusinessException;
import cart.exception.notfound.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(final AuthenticationException e) {
        log.warn("Exception from handleAuthenticationException = ", e);
        final ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorResponse);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationException(final AuthorizationException e) {
        log.warn("Exception from handleAuthorizationException = ", e);
        final ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(errorResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(final NotFoundException e) {
        log.warn("Exception from handleNotFoundException = ", e);
        final ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
        log.warn("Exception from handleBusinessException = ", e);
        final ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.warn("Exception from handleMethodArgumentNotValidException = ", e);
        final List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        final String errorMessage = fieldErrors.stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));
        final ErrorResponse errorResponse = new ErrorResponse(errorMessage);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        log.warn("Exception from handleMissingServletRequestParameterException = ", e);
        final ErrorResponse errorResponse = new ErrorResponse(
                "잘못된 요청입니다. 입력한 쿼리 파라미터 타입: " + e.getParameterName() +
                        "올바른 쿼리 파라미터 타입: " + e.getParameterType()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
        log.warn("Exception from handleHttpRequestMethodNotSupportedException = ", e);
        final ErrorResponse errorResponse = new ErrorResponse("잘못된 요청입니다. HTTP 메서드를 다시 확인해주세요. 입력한 HTTP 메서드: " + e.getMethod());
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(final Exception e) {
        log.error("Exception from handleUnexpectedException = ", e);
        final ErrorResponse errorResponse = new ErrorResponse("서버에 예상치 못한 문제가 발생하였습니다. 연락주세요 ㅎㅎㅎ.");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}
