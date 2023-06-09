package cart.domain.member;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class PasswordTest {

    @Test
    void 패스워드는_공백일_시_예외를_던진다() {
        assertThatThrownBy(() -> new Password(" "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Password 는 공백일 수 없습니다.");
    }

    @Test
    void 패스워드는_4자_보다_작을_시_예외를_던진다() {
        assertThatThrownBy(() -> new Password("e".repeat(3)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Password 는 4자 이상이어야 합니다.");
    }

    @Test
    void 패스워드는_공백이_아니면_예외를_던지지_않는다() {
        assertDoesNotThrow(() -> new Password("p".repeat(12)));
    }

}
