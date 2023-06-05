package cart.domain;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.DeductionCoupon;
import cart.domain.coupon.PercentCoupon;
import cart.domain.order.Order;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CouponTest {

    @Test
    void 퍼센트_할인_쿠폰이_적용된_할인가를_구한다() {
        Coupon percentCoupon = new PercentCoupon(1L, "10% 할인 쿠폰", 0.1);

        int discountMoney = percentCoupon.discount(10000);

        assertThat(discountMoney).isEqualTo(9000);
    }

    @Test
    void 차감_할인_쿠폰이_적용된_할인가를_구한다() {
        Coupon deductionCoupon = new DeductionCoupon(1L, "1000원 할인", 1000);

        int discountMoney = deductionCoupon.discount(10000);

        assertThat(discountMoney).isEqualTo(9000);

    }

    @Test
    void 쿠폰의_minimumPrice보다_낮은_가격의_주문에는_사용할수없다(){
        Order order = mock(Order.class);
        when(order.getOriginalPrice()).thenReturn(1000);
        Coupon coupon = new DeductionCoupon(1L, "1000원 할인", 1000, 2000);

        boolean usable = coupon.isUsable(order);

        assertThat(usable).isFalse();
    }

    @Test
    void 쿠폰의_minimumPrice_이상인_가격의_주문에만_사용할수있다(){
        Order order = mock(Order.class);
        when(order.getOriginalPrice()).thenReturn(1000);
        Coupon coupon = new DeductionCoupon(1L, "1000원 할인", 1000, 1000);

        boolean usable = coupon.isUsable(order);

        assertThat(usable).isTrue();
    }
}
