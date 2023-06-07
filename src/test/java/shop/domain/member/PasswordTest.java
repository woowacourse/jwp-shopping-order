package shop.domain.member;

import shop.exception.ShoppingException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PasswordTest {

    @ParameterizedTest(name = "비밀번호는 4글자 이상, 10글자 이하여야 한다.")
    @ValueSource(strings = {"1", "123", "", "이것은 열한글자입니다"})
    void createNaturalPasswordTest1(String password) {
        assertThatThrownBy(() -> Password.createFromNaturalPassword(password))
                .isInstanceOf(ShoppingException.class);
    }

    @ParameterizedTest(name = "비밀번호는 4글자 이상, 10글자 이하여야 한다.")
    @ValueSource(strings = {"네글자다", "이것은 열글자입니다"})
    void createNaturalPasswordTest2(String password) {
        assertDoesNotThrow(() -> Password.createFromNaturalPassword(password));
    }
}
