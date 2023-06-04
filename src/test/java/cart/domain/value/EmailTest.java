package cart.domain.value;

import cart.exception.value.NullOrBlankException;
import cart.exception.value.email.InvalidEmailException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class EmailTest {

    @ParameterizedTest(name = "{displayName}")
    @NullSource
    @ValueSource(strings = {" ", "", "   "})
    @DisplayName("이메일이 {0}이면 에러를 발생시킨다.")
    void validate_email_null_or_blank(String email) {
        // when + then
        assertThatThrownBy(() -> new Email(email))
                .isInstanceOf(NullOrBlankException.class);

    }

    @ParameterizedTest(name = "{displayName}")
    @ValueSource(strings = {"@naver.com", "ako", "ako#naver.com"})
    @DisplayName("올바르지 않은 이메일 형식({0})이면 에러를 발생한다.")
    void validate_email_form(String email) {
        // when + then
        assertThatThrownBy(() -> new Email(email))
                .isInstanceOf(InvalidEmailException.class);
    }

}
