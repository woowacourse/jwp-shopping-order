package cart.domain;

import cart.exception.PasswordInvalidException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PasswordTest {

    @DisplayName("패스워드가 공백이면 예외를 발생시킨다.")
    @EmptySource
    @ParameterizedTest
    void throws_exception_when_password_is_empty_or_null(final String password) {
        // when & then
        assertThatThrownBy(() -> new Password(password))
                .isInstanceOf(PasswordInvalidException.class);
    }

    @DisplayName("패스워드를 정상적으로 생성한다.")
    @Test
    void create_password_success() {
        // given
        String password = "1234";

        // when & then
        assertDoesNotThrow(() -> new Password(password));
    }
}
