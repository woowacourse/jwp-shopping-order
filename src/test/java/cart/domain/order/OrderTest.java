package cart.domain.order;

import cart.domain.Product;
import cart.domain.coupon.Coupon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {
    private Order order7500;
    private DeliveryFee deliveryFee;
    private Coupon coupon;

    @BeforeEach
    void setting() {
        final List<OrderProduct> products = List.of(
                new OrderProduct(new Product("A", 1000, "none"), 3),
                new OrderProduct(new Product("B", 500, "none"), 3)
        );
        order7500 = new Order(products, OrderPrice.of(products, new DeliveryFee(3000)));
        coupon = new Coupon(1L, 1000, "1000");
    }

    @DisplayName("주문에 쿠폰을 적용한다.")
    @Test
    void applyCoupon() {
        //given
        final Coupon coupon = new Coupon(1L, 1000, "1000");

        //when
        order7500.applyCoupon(coupon);

        //then
        assertThat(order7500.price()).isEqualTo(6500);
    }

}
