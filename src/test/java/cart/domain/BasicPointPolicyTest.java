package cart.domain;

import cart.domain.common.Money;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BasicPointPolicyTest {

    @Test
    void 금액의_일정_부분을_포인트로_적립한다() {
        //given
        final PointPolicy pointPolicy = new BasicPointPolicy();
        final Money money = Money.valueOf(10000);

        //when
        final Point point = pointPolicy.save(money);

        //then
        assertThat(point).isEqualTo(Point.valueOf(1000));
    }
}
