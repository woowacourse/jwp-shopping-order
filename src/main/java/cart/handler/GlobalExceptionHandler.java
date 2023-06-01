package cart.handler;

import cart.exception.AuthenticationException;
import cart.exception.CartItemNotFoundException;
import cart.exception.CouponCreateBadRequestException;
import cart.exception.CouponNotFoundException;
import cart.exception.EmailInvalidException;
import cart.exception.MemberAlreadyExistException;
import cart.exception.MemberNotFoundException;
import cart.exception.MemberNotOwnerException;
import cart.exception.PasswordInvalidException;
import cart.exception.QuantityExceedsCartException;
import cart.exception.SalePercentageInvalidRangeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        return responseBadRequest(Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handlerAuthenticationException(final AuthenticationException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
    }

    @ExceptionHandler(MemberNotOwnerException.class)
    public ResponseEntity<String> handleException(final MemberNotOwnerException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<String> handleCartItemNotFoundException(final CartItemNotFoundException exception) {
        return responseNotFound(exception.getMessage());
    }

    @ExceptionHandler(CouponNotFoundException.class)
    public ResponseEntity<String> handlerCouponNotFoundException(final CouponNotFoundException exception) {
        return responseNotFound(exception.getMessage());
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<String> handleMemberNotFoundException(final MemberNotFoundException exception) {
        return responseNotFound(exception.getMessage());
    }

    @ExceptionHandler(EmailInvalidException.class)
    public ResponseEntity<String> handleEmailInvalidException(final EmailInvalidException exception) {
        return responseBadRequest(exception.getMessage());
    }

    @ExceptionHandler(PasswordInvalidException.class)
    public ResponseEntity<String> handlePasswordInvalidException(final PasswordInvalidException exception) {
        return responseBadRequest(exception.getMessage());
    }

    @ExceptionHandler(QuantityExceedsCartException.class)
    public ResponseEntity<String> handleQuantityExceedsCartException(final QuantityExceedsCartException exception) {
        return responseBadRequest(exception.getMessage());
    }

    @ExceptionHandler(SalePercentageInvalidRangeException.class)
    public ResponseEntity<String> handleSalePercentageInvalidRangeException(final SalePercentageInvalidRangeException exception) {
        return responseBadRequest(exception.getMessage());
    }

    @ExceptionHandler(CouponCreateBadRequestException.class)
    public ResponseEntity<String> handlerCouponCreateBadRequestException(final CouponCreateBadRequestException exception) {
        return responseBadRequest(exception.getMessage());
    }

    @ExceptionHandler(MemberAlreadyExistException.class)
    public ResponseEntity<String> handlerMemberAlreadyExistException(final MemberAlreadyExistException exception) {
        return responseConflict(exception.getMessage());
    }

    private ResponseEntity<String> responseNotFound(final String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    private ResponseEntity<String> responseBadRequest(final String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    private ResponseEntity<String> responseConflict(final String message) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }
}
