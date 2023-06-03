package cart.domain.discountpolicy;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.Coupons;
import cart.domain.point.Point;
import cart.domain.point.PointHistories;
import cart.domain.point.PointHistory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

class CouponDiscountPolicyTest {

    @Test
    void calculatePayment() {
        Coupons coupons = new Coupons(Collections.emptyList());
        PointHistories point = new PointHistories(List.of(
                new PointHistory(1L, new Point(100), new Point(10)),
                new PointHistory(3L, new Point(100), new Point(10)
                )));

        Coupons coupons2 = new Coupons(List.of(
                new Coupon(1L, "a", 10, 0, 10000),
                new Coupon(2L, "b", 0, 1000, 10000),
                new Coupon(3L, "c", 0, 2000, 10000)));
        CouponDiscountPolicy couponDiscountPolicy = new CouponDiscountPolicy();
        int i = couponDiscountPolicy.calculatePayment(1000000, coupons, new Point(point.calculateTotalPoint()));
        Assertions.assertThat(i).isEqualTo(1000000);
    }
}
