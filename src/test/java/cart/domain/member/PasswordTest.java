package cart.domain.member;

import cart.exception.GlobalException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PasswordTest {
    @ParameterizedTest(name = "비밀번호는 4글자 이상, 10글자 이하여야 한다.")
    @ValueSource(strings = {"1", "123", "", "이것은 열한글자입니다"})
    void createMemberNameTest1(String password) {
        assertThatThrownBy(() -> new Password(password))
                .isInstanceOf(GlobalException.class);
    }

    @ParameterizedTest(name = "비밀번호는 4글자 이상, 10글자 이하여야 한다.")
    @ValueSource(strings = {"네글자다", "이것은 열글자입니다"})
    void createMemberNameTest2(String password) {
        assertDoesNotThrow(() -> new Password(password));
    }
}
