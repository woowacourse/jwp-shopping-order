package cart.domain;

import cart.exception.MemberException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberTest {

    private Long validId = 1L;
    private String validEmail = "a@a.com";
    private String validPassword = "1234a";

    @Nested
    class 이메일은 {

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
