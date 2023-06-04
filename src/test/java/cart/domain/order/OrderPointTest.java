package cart.domain.order;

import cart.domain.point.Point;
import cart.domain.point.PointPolicy;
import cart.domain.point.RatePointPolicy;
import cart.exception.customexception.CartException;
import cart.exception.customexception.ErrorCode;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static cart.fixture.MemberFixture.하디;
import static cart.fixture.TimestampFixture.*;
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

    @ParameterizedTest
    @CsvSource(value = {"0, 300, 50, 30", "29, 300, 50, 1", "30, 300, 50, 0", "250, 80, 50, 0", "330, 0, 50, 0", "370, 0, 10, 0", "380, 0, 0, 0"}, delimiter = ',')
    void 포인트를_사용한다(long 사용_포인트, long 포인트_1_남은_포인트, long 포인트_2_남은_포인트, long 포인트_3_남은_포인트) {
        // given
        Point 포인트_1 = new Point(1L, 300L, 300L, 하디, 만료일_1, 생성일_1);
        Point 포인트_2 = new Point(2L, 1000L, 50L, 하디, 만료일_2, 생성일_2);
        Point 포인트_3 = new Point(3L, 40L, 30L, 하디, 만료일_3, 생성일_3);

        // when
        List<Point> points = orderPoint.usePoint(사용_포인트, new ArrayList<>(List.of(포인트_1, 포인트_2, 포인트_3)));

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        for (Point point : points) {
            if (point.equals(포인트_1)) {
                softAssertions.assertThat(point.getLeftPoint()).isEqualTo(포인트_1_남은_포인트);
                continue;
            }
            if (point.equals(포인트_2)) {
                softAssertions.assertThat(point.getLeftPoint()).isEqualTo(포인트_2_남은_포인트);
                continue;
            }
            if (point.equals(포인트_3)) {
                softAssertions.assertThat(point.getLeftPoint()).isEqualTo(포인트_3_남은_포인트);
            }
        }
        softAssertions.assertAll();
    }
}
