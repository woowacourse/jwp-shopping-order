package cart.domain;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.DeductionCoupon;
import cart.domain.coupon.PercentCoupon;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CouponTest {

    @Test
    void 퍼센트_할인_쿠폰이_적용된_할인가를_구한다() {
        Coupon percentCoupon = new PercentCoupon(1L, "10% 할인 쿠폰", 0.1);
//
//        Member member = new Member(1L, "a@a.com", "1234");
//        OrderProduct orderProduct = new OrderProduct(1L, "치킨", 20000, "url", 1);
//        Order order = new Order(1L, 10000, 9000, List.of(orderProduct), percentCoupon, true, member);
        int discountMoney = percentCoupon.discount(10000);

        assertThat(discountMoney).isEqualTo(9000);
    }

    @Test
    void 차감_할인_쿠폰이_적용된_할인가를_구한다() {
        Coupon deductionCoupon = new DeductionCoupon(1L, "1000원 할인", 1000);

//        Member member = new Member(1L, "a@a.com", "1234");
//        OrderProduct orderProduct = new OrderProduct(1L, "치킨", 20000, "url", 1);
//        Order order = new Order(1L, 10000, 9000, List.of(orderProduct), deductionCoupon, true, member);

        int discountMoney = deductionCoupon.discount(10000);

        assertThat(discountMoney).isEqualTo(9000);

    }
}
