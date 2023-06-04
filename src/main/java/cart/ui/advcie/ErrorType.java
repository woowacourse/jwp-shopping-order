package cart.ui.advcie;

import cart.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.Set;

// TODO: 6/4/23 이 enum이 예외들을 관리 의존할 순 없나
public enum ErrorType {
    INTERNAL_SERVER_ERROR(
            Set.of(
                    NoExpectedException.class,
                    UnsupportedOperationException.class,
                    IllegalArgumentException.class
            ),
            HttpStatus.INTERNAL_SERVER_ERROR
    ),
    INVALID_FROM(
            Set.of(
                    MethodArgumentNotValidException.class
            ),
            HttpStatus.BAD_REQUEST
    ),
    UNAUTHORIZED(
            Set.of(
                    AuthenticationException.Unauthorized.class
            ),
            HttpStatus.UNAUTHORIZED
    ),
    FORBIDDEN(
            Set.of(
                    MemberCouponException.IllegalMember.class,
                    OrderException.IllegalMember.class,
                    CartItemException.IllegalMember.class
            ),
            HttpStatus.FORBIDDEN
    ),
    LOGIN_FAIL(
            Set.of(
                    AuthenticationException.LoginFail.class
            ),
            HttpStatus.BAD_REQUEST
    ),
    MEMBER_NO_EXIST(
            Set.of(
                    MemberException.NoExist.class
            ),
            HttpStatus.BAD_REQUEST
    ),
    PRODUCT_NO_EXIST(
            Set.of(
                    ProductException.NoExist.class
            ),
            HttpStatus.BAD_REQUEST
    ),
    CART_ITEM_NO_EXIST(
            Set.of(
                    CartItemException.NoExist.class
            ),
            HttpStatus.BAD_REQUEST
    ),
    ORDER_NO_EXIST(
            Set.of(
                    OrderException.NoExist.class
            ),
            HttpStatus.BAD_REQUEST
    ),
    COUPON_NO_EXIST(
            Set.of(
                    CouponException.NoExist.class
            ),
            HttpStatus.BAD_REQUEST
    ),
    CART_ITEM_ALREADY_EXIST(
            Set.of(
                    CartItemException.AlreadyExist.class
            ),
            HttpStatus.BAD_REQUEST
    ),
    CART_ITEM_EMPTY_CART(
            Set.of(
                    CartItemException.EmptyCart.class
            ),
            HttpStatus.BAD_REQUEST
    ),
    CART_ITEM_QUANTITY_INCORRECT(
            Set.of(
                    CartItemException.QuantityIncorrect.class
            ),
            HttpStatus.BAD_REQUEST
    ),
    CART_ITEM_PRICE_INCORRECT(
            Set.of(
                    CartItemException.PriceIncorrect.class
            ),
            HttpStatus.BAD_REQUEST
    ),
    COUPON_UNAVAILABLE(
            Set.of(
                    CouponException.Unavailable.class
            ),
            HttpStatus.BAD_REQUEST
    );

    private final Set<Class<?>> exceptions;
    private final HttpStatus httpStatus;

    ErrorType(final Set<Class<?>> exceptions, final HttpStatus httpStatus) {
        this.exceptions = exceptions;
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public static ErrorType from(final Exception exception) {
        Class<? extends Exception> exceptionClass = exception.getClass();
        return Arrays.stream(values())
                .filter(errorType -> errorType.exceptions.contains(exceptionClass))
                .findAny()
                .orElse(INTERNAL_SERVER_ERROR);
    }
}
