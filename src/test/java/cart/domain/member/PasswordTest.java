package cart.domain.member;

import cart.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static cart.exception.ErrorCode.INVALID_PASSWORD_LENGTH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PasswordTest {

    @DisplayName("Password가 정상적으로 생성된다.")
    @Test
    void password() {
        // when & then
        assertDoesNotThrow(() -> Password.encrypt("password"));
    }

    @DisplayName("Password가 4글자 미만 10글자 초과 시 INVALID_PASSWORD_LENGTH 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"3글자", "11글자비밀번호입니다"})
    void password_InvalidPasswordLength(String invalidPassword) {

        // when & then
        assertThatThrownBy(() -> Password.encrypt(invalidPassword))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(INVALID_PASSWORD_LENGTH);
    }

    @DisplayName("패스워드가 일치하는지에 따라 boolean을 반환한다.")
    @Test
    void checkPassword() {
        // given
        String samePassword = "password";
        String differentPassword = "1234";
        Password password = Password.of("password");

        // when & then
        assertThat(password.checkPassword(samePassword)).isTrue();
        assertThat(password.checkPassword(differentPassword)).isFalse();
    }
}
