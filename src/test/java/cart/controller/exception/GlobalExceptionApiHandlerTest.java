package cart.controller.exception;

import cart.dto.ErrorResponse;
import cart.exception.ExceptionType;
import cart.exception.MemberException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class GlobalExceptionApiHandlerTest {

    @Test
    public void 예외_타입에_따라_에러응답가_반환한다() {
        GlobalExceptionApiHandler exceptionHandler = new GlobalExceptionApiHandler();
        MemberException memberException = new MemberException(ExceptionType.NOT_FOUND_MEMBER);

        ResponseEntity<ErrorResponse> errorResponseResponseEntity = exceptionHandler.handleException(memberException);

        assertThat(errorResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(errorResponseResponseEntity.getBody().getErrorCode()).isEqualTo(1003);
    }

    @Test
    public void 서버_에러가_발생하면_500을_반환한다() {
        GlobalExceptionApiHandler exceptionHandler = new GlobalExceptionApiHandler();
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException();

        ResponseEntity<ErrorResponse> errorResponseResponseEntity = exceptionHandler.handleException(illegalArgumentException);

        assertThat(errorResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(errorResponseResponseEntity.getBody().getErrorCode()).isEqualTo(10000);
    }
}
