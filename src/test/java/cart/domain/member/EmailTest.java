package cart.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailTest {

    @DisplayName("이메일 형식이 유효하지 않을 경우 Exception이 발생한다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "a@aa", "a.com", "a.co.kr"})
    void invalidFormatTest(final String value) {
        assertThrows(IllegalArgumentException.class, () -> new Email(value));
    }

    @DisplayName("이메일 형식은 유효해야 한다.")
    @ParameterizedTest
    @ValueSource(strings = {"a@a.com", "a@co.kr"})
    void Should__When_(final String value) {
        final Email email = new Email(value);
        assertThat(email.getValue()).isEqualTo(value);
    }
}
