package cart.domain.member;

import cart.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static cart.exception.ErrorCode.INVALID_MEMBER_NAME_LENGTH;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MemberTest {

    @DisplayName("Member가 정상적으로 생성된다.")
    @Test
    void member() {
        // when & then
        assertDoesNotThrow(() -> new Member("name", Password.encrypt("password")));
    }

    @DisplayName("Member name이 1글자 미만 10글자 초과 시 INVALID_MEMBER_NAME_LENGTH 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "11글자아이디입니다."})
    void member_InvalidNameLength(String invalidName) {
        // when & then
        assertThatThrownBy(() -> new Member(invalidName, Password.encrypt("password")))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(INVALID_MEMBER_NAME_LENGTH);
    }
}
