package cart.domain.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.exception.BadRequestException;
import cart.exception.ErrorCode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class MemberPasswordTest {

    @ParameterizedTest(name = "4~10글자의 비밀번호가 들어오면 인코딩된 비밀번호가 생성된다.")
    @CsvSource(value = {"jour:b5977b836b445eafefdfebe5d6463785186ff90d78f03af3864190d729957cf2",
        "jourzura12:51174a81bf2ed14f632f76ca972bdb41a5f7b45fe7af310ae0aa17ffb919f919"}, delimiter = ':')
    void create(final String password, final String expected) {
        final MemberPassword memberPassword = assertDoesNotThrow(() -> MemberPassword.create(password));
        assertThat(memberPassword.getPassword())
            .isEqualTo(expected);
    }

    @ParameterizedTest(name = "4글자 미만, 10글자 초과의 비밀번호가 들어오면 예외가 발생한다.")
    @ValueSource(strings = {"jou", "jourzura123"})
    void create_fail(final String password) {
        assertThatThrownBy(() -> MemberPassword.create(password))
            .isInstanceOf(BadRequestException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.MEMBER_PASSWORD_LENGTH);
    }
}
