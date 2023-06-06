package cart.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MemberPasswordTest {

    @ParameterizedTest
    @ValueSource(strings = {"12345", "1234567890123"})
    @DisplayName("비밀번호 길이가 6 ~ 12자를 벗어나면 예외발생")
    public void validateExceptionTest(String password) {
        assertThatThrownBy(() -> MemberPassword.from(password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 비밀번호 형식입니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456", "123456789012"})
    @DisplayName("비밀번호 길이가 6 ~ 12자 사이면 정살 수행")
    public void validateTest(String password) {
        assertDoesNotThrow(() -> MemberPassword.from(password));
    }
}
