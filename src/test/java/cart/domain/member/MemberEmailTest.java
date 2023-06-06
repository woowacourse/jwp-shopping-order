package cart.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MemberEmailTest {

    @ParameterizedTest
    @ValueSource(strings = {"leo.com", "leo@.com"})
    @DisplayName("올바르지 않은 이메일의 경우 예외 발생")
    void validateEmailExceptionTest(String email) {
        assertThatThrownBy(() -> MemberEmail.from(email))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("올바른 이메일 형식이 아닙니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"leo@gmail.com", "leo@naver.com"})
    @DisplayName("올바른 이메일의 경우 정상수행")
    void validateEmailTest(String email) {
        assertDoesNotThrow(() -> MemberEmail.from(email));
    }
}
