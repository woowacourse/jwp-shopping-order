package cart.ui;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import cart.dto.ErrorResponse;
import cart.exception.ApplicationException;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnhandled(Exception e) {
        logger.error(e.getMessage(), e.getCause());
        e.printStackTrace();
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handlerAuthenticationException(ApplicationException e) {
        logger.error(e.getMessage(), e.getCause());
        return ResponseEntity.status(e.status()).body(new ErrorResponse(e));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoHandlerFound(NoHandlerFoundException e) {
        logger.warn(e.getMessage(), e.getCause());
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), "리소스를 찾지 못했습니다");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingRequestParameter(MissingServletRequestParameterException e) {
        logger.warn(e.getMessage(), e.getCause());
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "쿼리스트링 '" + e.getParameterName() + "'이 없거나 잘못됐습니다");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        logger.warn(e.getMessage(), e.getCause());
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "요청 페이로드를 읽지 못했습니다");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        logger.warn(e.getMessage(), e.getCause());
        String message = e.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message);
    }
}
