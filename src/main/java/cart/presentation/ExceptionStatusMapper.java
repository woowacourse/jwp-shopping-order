package cart.presentation;

import cart.exception.application.AuthenticationException;
import cart.exception.application.ExceedAvailablePointException;
import cart.exception.application.ExceedOwnedPointException;
import cart.exception.application.IllegalMemberException;
import cart.exception.application.PointInconsistentException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

public enum ExceptionStatusMapper {

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, List.of(
            AuthenticationException.class
    )),
    FORBIDDEN(HttpStatus.FORBIDDEN, List.of(
            IllegalMemberException.class
    )),
    CONFLICT(HttpStatus.CONFLICT, List.of(
            PointInconsistentException.class
    )),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, List.of(
            ExceedAvailablePointException.class,
            ExceedOwnedPointException.class
    )),
    ;

    private final HttpStatus httpStatus;
    private final List<Class <? extends Exception>> exceptions;

    ExceptionStatusMapper(HttpStatus httpStatus, List<Class<? extends Exception>> exceptions) {
        this.httpStatus = httpStatus;
        this.exceptions = exceptions;
    }

    public static HttpStatus of(Exception exception) {
        return Arrays.stream(values())
                .filter(value -> value.exceptions.contains(exception.getClass()))
                .findAny()
                .orElseThrow()
                .httpStatus;
    }
}
