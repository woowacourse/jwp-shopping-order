package cart.domain.order;

import cart.domain.point.Point;
import cart.domain.point.PointPolicy;
import cart.domain.point.RatePointPolicy;
import cart.exception.customexception.CartException;
import cart.exception.customexception.ErrorCode;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static cart.fixture.MemberFixture.하디;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderPointTest {

    private PointPolicy pointPolicy = new RatePointPolicy();
    private OrderPoint orderPoint = new OrderPoint(pointPolicy);

    @Test
    void 적립_포인트를_계산한다() {
        // given
        Long usedPoint = 100L;
        Long totalPrice = 200L;
        Timestamp current = new Timestamp(System.currentTimeMillis());
        Long expectedEarnedPoint = (long) (Math.ceil(usedPoint * RatePointPolicy.EARNING_RATE / 100.0));

        // when
        Point point = orderPoint.earnPoint(하디, usedPoint, totalPrice, current);

        // then
        assertThat(point.getEarnedPoint()).isEqualTo(expectedEarnedPoint);
    }

    @Test
    void 총가격보다_쓴_포인트가_더크면_예외가_발생한다() {
        // given
        Long usedPoint = 300L;
        Long totalPrice = 200L;
        Timestamp current = new Timestamp(System.currentTimeMillis());

        // when, then
        assertThatThrownBy(() -> orderPoint.earnPoint(하디, usedPoint, totalPrice, current))
                .isInstanceOf(CartException.class)
                .satisfies(exception -> {
                    CartException cartException = (CartException) exception;
                    assertThat(cartException.getErrorCode()).isEqualTo(ErrorCode.POINT_EXCEED_TOTAL_PRICE);
                });
    }
}
