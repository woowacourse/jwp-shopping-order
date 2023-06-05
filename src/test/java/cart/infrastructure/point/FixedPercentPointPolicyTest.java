package cart.infrastructure.point;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.price.Point;
import cart.domain.price.PointPolicy;
import cart.domain.price.Price;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FixedPercentPointPolicyTest {

    @ParameterizedTest
    @CsvSource(value = {"10,500", "1,50", "0.1,5"})
    void 금액의_일정_비율만큼_포인트가_계산된다(double percent, long point) {
        PointPolicy pointPolicy = new FixedPercentPointPolicy(percent);

        Point rewardPoint = pointPolicy.calculate(Price.from(5000));

        assertThat(rewardPoint.getAmount()).isEqualTo(point);
    }

    @Test
    void 금액이_0원이면_적립_포인트가_0원이어야_한다() {
        PointPolicy pointPolicy = new FixedPercentPointPolicy(10);

        Point rewardPoint = pointPolicy.calculate(Price.ZERO);

        assertThat(rewardPoint)
                .isEqualTo(Point.ZERO);
    }
}
