package cart.ui.exception;

import cart.application.response.ErrorResponse;
import cart.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        logger.info(e.getMessage());

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(ErrorResponse.from(BAD_REQUEST.name(), e.getMessage(), now()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(Exception e) {
        logger.info(e.getMessage());

        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(ErrorResponse.from(UNAUTHORIZED.name(), e.getMessage(), now()));
    }

    @ExceptionHandler({
            CartItemException.InvalidMember.class,
            MemberException.class
    })
    public ResponseEntity<ErrorResponse> handleForbidden(Exception e) {
        logger.info(e.getMessage());

        return ResponseEntity
                .status(FORBIDDEN)
                .body(ErrorResponse.from(FORBIDDEN.name(), e.getMessage(), now()));
    }

    @ExceptionHandler({
            CartItemException.class,
            MemberException.NotEnoughPoint.class,
            MemberException.NotEnoughMoney.class,
            OrderException.class,
            ProductException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception e) {
        logger.info(e.getMessage());

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(ErrorResponse.from(BAD_REQUEST.name(), e.getMessage(), now()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(RuntimeException e) {
        logger.warn(e.getMessage());

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(ErrorResponse.from(BAD_REQUEST.name(), "잘못된 클라이언트의 요청입니다.", now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        logger.error(e.getMessage());

        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.from(INTERNAL_SERVER_ERROR.name(), "서버 내부에서 오류가 발생하였습니다.", now()));
    }
}
