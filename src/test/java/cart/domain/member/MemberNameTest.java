package cart.domain.member;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.exception.BadRequestException;
import cart.exception.ErrorCode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MemberNameTest {

    @ParameterizedTest(name = "4~10글자의 이름이 들어오면 정상적으로 생성된다.")
    @ValueSource(strings = {"jour", "jourzura12"})
    void create(final String name) {
        assertDoesNotThrow(() -> MemberName.create(name));
    }

    @ParameterizedTest(name = "4글자 미만, 10글자 초과의 이름이 들어오면 예외가 발생한다.")
    @ValueSource(strings = {"jou", "jourzura123"})
    void create_fail(final String name) {
        assertThatThrownBy(() -> MemberName.create(name))
            .isInstanceOf(BadRequestException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.MEMBER_NAME_LENGTH);
    }
}
