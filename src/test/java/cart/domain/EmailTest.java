package cart.domain;

import cart.domain.member.Email;
import cart.exception.EmailInvalidException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class EmailTest {

    @DisplayName("이메일의 형식이 맞지 않으면 예외를 발생한다.")
    @ValueSource(strings = {"sosow", "", "sosow0212@naver"})
    @ParameterizedTest
    void throws_exception_when_email_invalid_style(final String email) {
        // when & then
        assertThatThrownBy(() -> new Email(email))
                .isInstanceOf(EmailInvalidException.class);
    }

    @DisplayName("이메일을 정상적으로 생성한다.")
    @Test
    void create_email_success() {
        // given
        String email = "sosow0212@naver.com";

        // when & then
        assertDoesNotThrow(() -> new Email(email));
    }
}
