package cart.exception;

import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception ex) {
        logger.error("", ex);

        return ResponseEntity
                .internalServerError()
                .body(new ExceptionResponse("예상치 못한 문제가 발생했습니다."));
    }

    @ExceptionHandler(MoneyException.DecimalMoney.class)
    public ResponseEntity<ExceptionResponse> handleDecimalMoneyException(MoneyException.DecimalMoney ex) {
        logger.error("", ex);

        return ResponseEntity
                .internalServerError()
                .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler({
            AuthenticationException.InvalidTokenFormat.class,
            AuthenticationException.ForbiddenMember.class,
            AuthenticationException.InvalidMember.class,
    })
    public ResponseEntity<ExceptionResponse> handlerAuthenticationException(AuthenticationException ex) {
        logger.info("", ex);

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler({
            CartItemException.IllegalMember.class,
            OrderException.IllegalMember.class
    })
    public ResponseEntity<ExceptionResponse> handleForbiddenException(RuntimeException ex) {
        logger.info("", ex);

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ExceptionResponse("잘못된 사용자입니다."));
    }

    @ExceptionHandler({
            CartItemException.AlreadyExist.class,
            CartItemException.InvalidQuantity.class,
            CartItemException.InvalidIdsFormat.class,
            CartItemException.DuplicateIds.class,
            MemberException.TooManyUsedPoints.class,
            MoneyException.Negative.class,
            MoneyException.MultiplyZeroOrNegative.class,
            OrderException.InvalidQuantity.class,
            PointException.InvalidPolicy.class,
    })
    public ResponseEntity<ExceptionResponse> handleBadRequestException(RuntimeException ex) {
        logger.info("", ex);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler({CartItemException.NotFound.class,
            ProductException.NotFound.class,
            MemberException.NotFound.class,
            OrderException.NotFound.class
    })
    public ResponseEntity<ExceptionResponse> handleNotFoundException(RuntimeException ex) {
        logger.info("", ex);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(ex.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        logger.info("", ex);

        String fieldErrorMessage = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(System.lineSeparator()));

        return ResponseEntity
                .badRequest()
                .body(new ExceptionResponse(fieldErrorMessage));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            Object body,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        logger.warn("", ex);

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }
}
