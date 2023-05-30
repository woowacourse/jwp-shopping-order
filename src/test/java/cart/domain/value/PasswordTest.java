package cart.domain.value;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PasswordTest {

    @DisplayName("비밀번호는 8~16자의 영문 대 소문자, 숫자, 특수문자를 사용하지 않으면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"1234", "abcdef", "abcdefghijklmnopqrstuvwxyz", "123@fgg"})
    void validate(String input) {
        // when, then
        assertThatThrownBy(() -> new Password(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호는 8~16자의 영문 대 소문자, 숫자, 특수문자를 사용해야합니다.");
    }
}
