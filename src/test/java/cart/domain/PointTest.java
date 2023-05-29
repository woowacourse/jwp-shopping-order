package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PointTest {

    @Test
    void 포인트가_정상적으로_생성된다() {
        long amount = 100;

        Point point = new Point(amount);

        assertThat(point.getAmount()).isEqualTo(amount);
    }

    @Test
    void 포인트에_음수가_할당되면_예외가_발생한다() {
        long amount = -1;

        assertThatThrownBy(() -> new Point(amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("포인트는 음수가 될 수 없습니다.");
    }
}
