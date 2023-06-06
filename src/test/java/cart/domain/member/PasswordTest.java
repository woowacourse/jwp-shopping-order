package cart.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PasswordTest {

    private static final String DEFAULT_PASSWORD = "password";

    @DisplayName("비밀번호가 비어있을 경우 Exception이 발생한다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    void invalidPasswordTest(final String value) {
        assertThrows(IllegalArgumentException.class, () -> new Password(value));
    }

    @DisplayName("비밀번호는 비어있을 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"password", "1234"})
    void validPasswordTest(final String value) {
        final Password password = new Password(value);
        assertThat(password.getValue()).isEqualTo(value);
    }

    @DisplayName("비밀번호가 같을 경우 true를 반환한다.")
    @Test
    void checkSamePassword() {
        final Password password = new Password(DEFAULT_PASSWORD);
        assertThat(password.checkPassword(DEFAULT_PASSWORD)).isTrue();
    }

    @DisplayName("비밀번호가 다를 경우 false를 반환한다.")
    @Test
    void checkDifferencePassword() {
        final Password password = new Password(DEFAULT_PASSWORD);
        assertThat(password.checkPassword("1234")).isFalse();
    }
}
