package cart.common;

import cart.exception.AuthenticationException;
import cart.exception.CartItemCalculateException;
import cart.exception.CartItemNotFoundException;
import cart.exception.CouponNotFoundException;
import cart.exception.DiscountOverPriceException;
import cart.exception.NotExitingCouponIssueException;
import cart.exception.OrderAuthorizationException;
import cart.exception.ProductNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handle(final AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<String> handleCartException(final RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handle(final ProductNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(CouponNotFoundException.class)
    public ResponseEntity<String> handle(final CouponNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(CartItemCalculateException.class)
    public ResponseEntity<String> handle(final CartItemCalculateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(OrderAuthorizationException.class)
    public ResponseEntity<String> handle(final OrderAuthorizationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NotExitingCouponIssueException.class)
    public ResponseEntity<String> handle(final NotExitingCouponIssueException e) {
        LOGGER.error(e.getMessage());
        return ResponseEntity.internalServerError().build();
    }

    @ExceptionHandler(DiscountOverPriceException.class)
    public ResponseEntity<String> handle(final DiscountOverPriceException e) {
        LOGGER.error(e.getMessage());
        return ResponseEntity.internalServerError().build();
    }

}
