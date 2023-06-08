package cart.controller;

import static org.assertj.core.api.Assertions.assertThat;

import cart.dto.ErrorResponse;
import cart.exception.CartItemException;
import cart.exception.ExceptionType;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class GlobalExceptionHandlerTest {

    @Test
    void 예외_응답을_반환한다() {
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
        CartItemException cartItemException = new CartItemException(ExceptionType.NO_AUTHORITY_CART_ITEM);

        ResponseEntity<ErrorResponse> errorResponseResponseEntity = exceptionHandler.handleException(cartItemException);

        assertThat(errorResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(errorResponseResponseEntity.getBody().getErrorCode()).isEqualTo(1002);
    }

    @Test
    void 서버_오류가_발생하면_서버_예외_응답을_반환한다() {
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
        Exception exception = new Exception("서버 오류");

        ResponseEntity<ErrorResponse> errorResponseResponseEntity = exceptionHandler.handleException(exception);

        assertThat(errorResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(errorResponseResponseEntity.getBody().getErrorCode()).isEqualTo(999);
    }
}
