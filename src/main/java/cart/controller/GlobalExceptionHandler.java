package cart.controller;

import cart.controller.dto.response.ExceptionResponse;
import cart.exception.AuthorizationException;
import cart.exception.CartItemNotFoundException;
import cart.exception.InvalidPriceException;
import cart.exception.MemberNotFoundException;
import cart.exception.NotEnoughQuantityException;
import cart.exception.NotOwnerException;
import cart.exception.PaymentAmountNotEqualException;
import cart.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponse handleAuthorization(AuthorizationException e) {
        return new ExceptionResponse(e.getMessage());
    }

    @ExceptionHandler(NotOwnerException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse handleNotOwner(NotOwnerException e) {
        return new ExceptionResponse(e.getMessage());
    }

    @ExceptionHandler({MemberNotFoundException.class, ProductNotFoundException.class, CartItemNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleNotFound(IllegalArgumentException e) {
        return new ExceptionResponse(e.getMessage());
    }

    @ExceptionHandler({PaymentAmountNotEqualException.class, NotEnoughQuantityException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleBadRequest(IllegalArgumentException e) {
        return new ExceptionResponse(e.getMessage());
    }
}
