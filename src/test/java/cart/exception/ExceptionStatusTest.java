package cart.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ExceptionStatusTest {

    @Test
    void 에러_타입으로_에러_status를_찾는다() {
        ExceptionStatus exceptionStatus = ExceptionStatus.findByType(ExceptionType.NO_AUTHORITY_COUPON);

        assertThat(exceptionStatus.getHttpStatus()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(exceptionStatus.getErrorCode()).isEqualTo(4006);
    }
}
