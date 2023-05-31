package cart.domain.order;

import cart.domain.Coupon;
import cart.domain.Product;
import cart.exception.CouponDiscountOverPriceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {
    private Order order4500;
    private DeliveryFee deliveryFee;
    private Coupon coupon;

    @BeforeEach
    void setting() {
        order4500 = new Order(List.of(
                new OrderProduct(new Product("A", 1000, "none"), 3),
                new OrderProduct(new Product("B", 500, "none"), 3)
        ));
        deliveryFee = new DeliveryFee(3000);
        coupon = new Coupon(1L, 1000, "1000");
    }

    @DisplayName("주문에 배달료를 적용한다.")
    @Test
    void applyDeliveryFee() {
        //given, when
        order4500.applyDeliveryFee(deliveryFee);

        //then
        assertThat(order4500.price()).isEqualTo(7500);
    }

    @DisplayName("주문에 쿠폰을 적용한다.")
    @Test
    void applyCoupon() {
        //given
        final Coupon coupon = new Coupon(1L, 1000, "1000");

        //when
        order4500.applyCoupon(coupon);

        //then
        assertThat(order4500.price()).isEqualTo(3500);
    }

    @DisplayName("배달료를 적용시키고 쿠폰을 적용시킨다.")
    @Test
    void scenario_applyDeliveryFeeAndCoupon() {
        //given, when
        order4500.applyDeliveryFee(deliveryFee);
        order4500.applyCoupon(coupon);

        //then
        assertThat(order4500.price()).isEqualTo(6500);
    }

    @DisplayName("배달료를 적용시키고 쿠폰을 적용시킨다.")
    @Test
    void scenario_invalid_applyDeliveryFeeAndCoupon_overDiscount() {
        //given
        final Coupon coupon10000 = new Coupon(1L, 10000, "10000");

        //when
        order4500.applyDeliveryFee(deliveryFee);

        //then
        assertThatThrownBy(() -> order4500.applyCoupon(coupon10000))
                .isInstanceOf(CouponDiscountOverPriceException.class);
    }
}
