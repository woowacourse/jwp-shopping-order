package cart.domain;

import cart.exception.MemberException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberTest {

    private final Long validId = 1L;
    private final String validEmail = "a@a.com";
    private final String validPassword = "1234a";

    @Nested
    class 이메일은 {

        @Test
        void 정상적인_이메일이_입력된다() {
            Assertions.assertDoesNotThrow(() -> new Member(validId, validEmail, validPassword));
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "a"})
        void 올바른_형식이_아니면_예외를_던진다(String invalidEmail) {
            assertThatThrownBy(() -> new Member(validId, invalidEmail, validPassword))
                    .isInstanceOf(MemberException.InvalidEmail.class)
                    .hasMessageContaining("잘못된 이메일 형식입니다.");
        }

        @Test
        void 입력하지_않으면_예외를_던진다() {
            assertThatThrownBy(() -> new Member(validId, null, validPassword))
                    .isInstanceOf(MemberException.InvalidEmail.class)
                    .hasMessageContaining("잘못된 이메일 형식입니다.");
        }
    }
}
