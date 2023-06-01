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
        final String message = "[ERROR] 서버가 응답할 수 없습니다.";
        logger.error(message);
        return this.handleExceptionInternal(exception, null, HttpHeaders.EMPTY, HttpStatus.INTERNAL_SERVER_ERROR, request);
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

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            final Exception ex,
            final Object body,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request
    ) {
        if (ex instanceof DiscordException) {
            logger.error("[ERROR] " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse("[ERROR] " + ex.getMessage()));
        }

        if (ex instanceof IllegalArgumentException) {
            logger.error("[ERROR] " + ex.getMessage());
            return ResponseEntity.badRequest().body(new ExceptionResponse("[ERROR] " + ex.getMessage()));
        }
        
        if (ex instanceof AuthenticationException) {
            logger.error("[ERROR] " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionResponse("[ERROR] " + ex.getMessage()));
        }
        
        if (ex instanceof CartItemException.IllegalMember) {
            logger.error("[ERROR] " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionResponse("[ERROR] " + ex.getMessage()));
        }

        logger.error("[ERROR] 서버가 응답할 수 없습니다.");
        return ResponseEntity.internalServerError().body(new ExceptionResponse("[ERROR] 서버가 응답할 수 없습니다."));
    }
}
