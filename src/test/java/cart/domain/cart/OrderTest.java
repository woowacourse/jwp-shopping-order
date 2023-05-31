package cart.domain.cart;

import static cart.domain.coupon.DiscountPolicyType.DELIVERY;
import static cart.domain.coupon.DiscountPolicyType.PERCENT;
import static cart.domain.coupon.DiscountPolicyType.PRICE;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.common.Money;
import cart.domain.coupon.Coupon;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderTest {

    @Test
    void 주문_상품의_총합을_계산한다() {
        // given
        final Product product1 = new Product("pizza1", "pizza1.jpg", 8900L);
        final Product product2 = new Product("pizza2", "pizza2.jpg", 36500L);
        final CartItem cartItem1 = new CartItem(null, 3, null, product1);
        final CartItem cartItem2 = new CartItem(null, 4, null, product2);
        final Order order = Order.of(null, 1L, List.of(cartItem1, cartItem2));

        // when
        final Money result = order.calculateTotalPrice();

        // then
        assertThat(result).isEqualTo(Money.from(172700L));
    }

    @Test
    void 주문_상품의_할인_금액을_계산한다() {
        // given
        final Product product1 = new Product("pizza1", "pizza1.jpg", 8900L);
        final Product product2 = new Product("pizza2", "pizza2.jpg", 36500L);
        final CartItem cartItem1 = new CartItem(null, 3, null, product1);
        final CartItem cartItem2 = new CartItem(null, 4, null, product2);
        final Coupon coupon = new Coupon(1L, "20% 할인 쿠폰", PERCENT, 20L, Money.ZERO);
        final MemberCoupon memberCoupon = new MemberCoupon(1L, coupon);
        final Order order = Order.of(memberCoupon, 1L, List.of(cartItem1, cartItem2));

        // when
        final Money result = order.calculateDiscountPrice();

        // then
        assertThat(result).isEqualTo(Money.from(34540));
    }

    @Test
    void 주문_상품의_할인_금액은_총_상품금액보다_클_수_없다() {
        // given
        final Product product1 = new Product("pizza1", "pizza1.jpg", 8900L);
        final Product product2 = new Product("pizza2", "pizza2.jpg", 36500L);
        final CartItem cartItem1 = new CartItem(null, 3, null, product1);
        final CartItem cartItem2 = new CartItem(null, 4, null, product2);
        final Coupon coupon = new Coupon(1L, "20만원 할인 쿠폰", PRICE, 200000L, Money.ZERO);
        final MemberCoupon memberCoupon = new MemberCoupon(1L, coupon);
        final Order order = Order.of(memberCoupon, 1L, List.of(cartItem1, cartItem2));

        // when
        final Money result = order.calculateDiscountPrice();

        // then
        assertThat(result).isEqualTo(Money.from(172700));
    }

    @Test
    void 주문_상품의_배달비를_계산한다() {
        // given
        final Product product = new Product("pizza1", "pizza1.jpg", 8900L);
        final CartItem cartItem = new CartItem(null, 3, null, product);
        final Coupon coupon = new Coupon(1L, "배달비 할인 쿠폰", DELIVERY, 3000L, Money.from(3000L));
        final MemberCoupon memberCoupon = new MemberCoupon(1L, coupon);
        final Order order = Order.of(memberCoupon, 1L, List.of(cartItem));

        // when
        final Money result = order.calculateDeliveryFee();

        // then
        assertThat(result).isEqualTo(Money.ZERO);
    }

}
