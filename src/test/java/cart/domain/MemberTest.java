package cart.domain;

import cart.exception.MemberException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static cart.fixture.MoneyFixture.금액;
import static cart.fixture.MoneyFixture.포인트;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;

@DisplayNameGeneration(ReplaceUnderscores.class)
class MemberTest {

    private String 비밀번호 = "1234a";

    @Nested
    class 이메일은 {

        @ParameterizedTest
        @ValueSource(strings = {"", "a"})
        void 올바른_형식이_아니면_예외를_던진다(String 잘못된_이메일) {
            assertThatThrownBy(() -> new Member(잘못된_이메일, 비밀번호, 금액("1000"), 포인트("1000")))
                    .isInstanceOf(MemberException.InvalidEmail.class)
                    .hasMessageContaining("이메일 형식을 확인해주세요.");
        }

        @Test
        void 입력하지_않으면_예외를_던진다() {
            assertThatThrownBy(() -> new Member(null, 비밀번호, 금액("1000"), 포인트("1000")))
                    .isInstanceOf(MemberException.InvalidEmail.class)
                    .hasMessageContaining("이메일 형식을 확인해주세요.");
        }
    }
}
