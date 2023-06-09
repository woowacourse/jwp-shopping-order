package cart.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.ErrorMessage;
import cart.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("Quantity 단위 테스트")
class PointTest {
    @Test
    void 포인트_생성시_음수가_입력되면_예외를_반환한다() {
        // then
        assertThatThrownBy(() -> new Point(-20))
                .isInstanceOf(MemberException.class)
                .hasMessage(ErrorMessage.INVALID_POINT.getMessage());
    }
}
