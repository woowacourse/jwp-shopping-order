package cart.ui;

import cart.exception.AuthenticationException;
import cart.exception.AuthorizationException;
import cart.exception.BadRequestException;
import cart.exception.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import static cart.exception.ErrorCode.HTTP_REQUEST_EXCEPTION;
import static cart.exception.ErrorCode.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleBindException(final BindException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> errorMessages = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            errorMessages.add(fieldError.getDefaultMessage());
            logger.error(fieldError.getDefaultMessage());
        }
        return ResponseEntity.badRequest()
                .body(errorMessages);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(final ServletRequestBindingException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.error(ex.getMessage());
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(HTTP_REQUEST_EXCEPTION, ex.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.error(ex.getMessage());
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(HTTP_REQUEST_EXCEPTION, ex.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.error(ex.getMessage());
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(HTTP_REQUEST_EXCEPTION, ex.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handlerAuthenticationException(AuthenticationException e) {
        logger.error(e.getErrorCode().getErrorMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(e.getErrorCode(), e.getErrorCode().getErrorMessage()));
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleException(AuthorizationException e) {
        logger.error(e.getErrorCode().getErrorMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(e.getErrorCode(), e.getErrorCode().getErrorMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleException(BadRequestException e) {
        logger.error(e.getErrorCode().getErrorMessage());
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getErrorCode(), e.getErrorCode().getErrorMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        e.printStackTrace();
        logger.error(e.getMessage());
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.getErrorMessage()));
    }
}
