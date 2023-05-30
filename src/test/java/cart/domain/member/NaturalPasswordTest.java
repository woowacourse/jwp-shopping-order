package cart.domain.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.exception.BadRequestException;
import cart.exception.ErrorCode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class NaturalPasswordTest {

    @ParameterizedTest(name = "4글자 미만, 10글자 초과의 비밀번호가 들어오면 예외가 발생한다.")
    @ValueSource(strings = {"jou", "jourzura123"})
    void create_fail(final String password) {
        assertThatThrownBy(() -> NaturalPassword.create(password))
            .isInstanceOf(BadRequestException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.MEMBER_PASSWORD_LENGTH);
    }
}
