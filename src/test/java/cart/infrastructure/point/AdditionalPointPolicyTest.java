package cart.infrastructure.point;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.price.Point;
import cart.domain.price.PointPolicy;
import cart.domain.price.Price;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AdditionalPointPolicyTest {

    @Test
    void 추가적인_포인트_정책이_정상적으로_동작해야한다() {
        PointPolicy fixedPercentPointPolicy = new FixedPercentPointPolicy(10);
        PointPolicy fixedAmountPointPolicy = new FixedAmountPointPolicy(100);
        PointPolicy additionalPointPolicy = new AdditionalPointPolicy(fixedPercentPointPolicy,
                fixedAmountPointPolicy);

        Point point = additionalPointPolicy.calculate(Price.from(1000));

        assertThat(point.getAmount())
                .isEqualTo(200);
    }
}
