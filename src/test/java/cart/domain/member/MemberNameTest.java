package cart.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MemberNameTest {

    @DisplayName("멤버의 이름이 1 ~ 30자를 벗어나면 예외발생")
    @ParameterizedTest
    @ValueSource(strings = {"", "1234567890123456789012345678901"})
    void validateExceptionTest(String memberName) {
        assertThatThrownBy(() -> MemberName.from(memberName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("멤버의 이름은 1 ~ 30글자 사이만 가능합니다.");
    }

    @DisplayName("멤버 이름이 1 ~ 30글자 사이면 정상 수행된다.")
    @ParameterizedTest
    @ValueSource(strings = {"1", "123456789012345678901234567890"})
    void validateTest(String memberName) {
        assertDoesNotThrow(() -> MemberName.from(memberName));
    }
}
