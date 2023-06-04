package cart.infrastructure.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.domain.price.Point;
import cart.domain.price.PointPolicy;
import cart.domain.price.Price;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FixedAmountPointPolicyTest {

    @Test
    void 금액에_상관없이_포인트가_반환되어야_한다() {
        PointPolicy fixedAmountPointPolicy = new FixedAmountPointPolicy(100);

        Point point = fixedAmountPointPolicy.calculate(Price.ZERO);

        assertThat(point.getAmount())
                .isEqualTo(100);
    }

    @Test
    void 지급되는_포인트가_음수이면_예외가_발생해야_한다() {
        assertThatThrownBy(() -> new FixedAmountPointPolicy(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("음수는 입력될 수 없습니다.");
    }
}
