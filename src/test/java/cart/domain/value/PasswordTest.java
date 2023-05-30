package cart.domain.value;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class PasswordTest {

    @ParameterizedTest(name = "{displayName}")
    @NullSource
    @ValueSource(strings = {" ", "", "   ", "asbcs", "12345", "@ab"})
    @DisplayName("비밀번호가 {0}이면 에러를 발생시킨다.")
    void validate_email_null_or_blank(String email) {
        // when + then
        assertThatThrownBy(() -> new Email(email))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
