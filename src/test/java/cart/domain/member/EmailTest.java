package cart.domain.member;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class EmailTest {

    @Test
    void 이메일은_공백일_시_예외를_던진다() {
        assertThatThrownBy(() -> new Email(" "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email 은 공백일 수 없습니다.");
    }

    @Test
    void 이메일은_254자를_넘을_시_예외를_던진다() {
        assertThatThrownBy(() -> new Email("e".repeat(255)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email 은 254자를 넘을 수 없습니다.");
    }

    @Test
    void 이메일_형식이_아닐_시_예외를_던진다() {
        assertThatThrownBy(() -> new Email("blackcat.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email 형식을 입력해주세요.");
    }

    @Test
    void 이메일은_공백이_아니면_예외를_던지지_않는다() {
        assertDoesNotThrow(() -> new Email("bk@wooteco.com"));
    }
}
