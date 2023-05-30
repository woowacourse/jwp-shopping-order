package cart.domain;

import cart.exception.MemberException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {

    private Long validId = 1L;
    private String validEmail = "a@a.com";
    private String validPassword = "1234a";

    @Test
    void 아이디를_입력하지_않으면_예외를_던진다() {
        assertThatThrownBy(() -> new Member(null, validEmail, validPassword))
                .isInstanceOf(MemberException.InvalidIdByNull.class)
                .hasMessageContaining("멤버 아이디를 입력해야 합니다.");
    }

    @Nested
    class 이메일은 {

        @ParameterizedTest
        @ValueSource(strings = {"", "a"})
        void 올바른_형식이_아니면_예외를_던진다(String invalidEmail) {
            assertThatThrownBy(() -> new Member(validId, invalidEmail, validPassword))
                    .isInstanceOf(MemberException.InvalidEmail.class)
                    .hasMessageContaining("이메일 형식을 확인해주세요.");
        }

        @Test
        void 입력하지_않으면_예외를_던진다() {
            assertThatThrownBy(() -> new Member(validId, null, validPassword))
                    .isInstanceOf(MemberException.InvalidEmail.class)
                    .hasMessageContaining("이메일 형식을 확인해주세요.");
        }
    }
}
