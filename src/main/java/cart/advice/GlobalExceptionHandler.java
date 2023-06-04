package cart.advice;

import cart.dto.ExceptionResponse;
import cart.exception.AuthenticationException;
import cart.exception.CartItemException;
import cart.exception.DiscordException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleInternalServerException(final Exception exception, final WebRequest request) {
        logger.error("[ERROR] " + exception.getMessage());
        return handleExceptionInternal(exception, new ExceptionResponse("[ERROR] " + exception.getMessage()), HttpHeaders.EMPTY, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            final TypeMismatchException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request
    ) {
        final String message = "[ERROR] 파라미터 타입과 일치하지 않습니다.";
        logger.error(message);
        return ResponseEntity.badRequest().body(new ExceptionResponse(message));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            final HttpMessageNotReadableException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request) {
        final String message = "[ERROR] 해당 파라미터로 변환할 수 없습니다.";
        logger.error(message);
        return ResponseEntity.badRequest().body(new ExceptionResponse(message));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request
    ) {
        final String exceptionMessage = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(System.lineSeparator() + "[ERROR] ", "[ERROR] ", ""));

        logger.error(exceptionMessage);
        return ResponseEntity.badRequest().body(new ExceptionResponse(exceptionMessage));
    }
    
    @ExceptionHandler(DiscordException.class)
    public ResponseEntity<Object> handleDiscordException(final DiscordException ex) {
        return buildResponseEntity(ex, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(final IllegalArgumentException ex) {
        return buildResponseEntity(ex, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(final AuthenticationException ex) {
        return buildResponseEntity(ex, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(CartItemException.IllegalMember.class)
    public ResponseEntity<Object> handleIllegalMemberException(final CartItemException.IllegalMember ex) {
        return buildResponseEntity(ex, HttpStatus.FORBIDDEN);
    }
    
    private ResponseEntity<Object> buildResponseEntity(final Exception ex, final HttpStatus status) {
        logger.error("[ERROR] " + ex.getMessage());
        return ResponseEntity.status(status).body(new ExceptionResponse("[ERROR] " + ex.getMessage()));
    }
}
