package cart.domain.cart;

import static cart.fixture.CouponFixture._20만원_할인_쿠폰;
import static cart.fixture.CouponFixture._20프로_할인_쿠폰;
import static cart.fixture.CouponFixture._3만원_이상_배달비_3천원_할인_쿠폰;
import static cart.fixture.ProductFixture.상품_18900원;
import static cart.fixture.ProductFixture.상품_8900원;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.VO.Money;
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
        final CartItem cartItem1 = new CartItem(null, 3, null, 상품_8900원);
        final CartItem cartItem2 = new CartItem(null, 4, null, 상품_18900원);
        final Order order = Order.of(null, 1L, List.of(cartItem1, cartItem2));

        // when
        final Money result = order.calculateTotalPrice();

        // then
        assertThat(result).isEqualTo(Money.from(102300L));
    }

    @Test
    void 주문_상품의_할인_금액을_계산한다() {
        // given
        final CartItem cartItem1 = new CartItem(null, 3, null, 상품_8900원);
        final CartItem cartItem2 = new CartItem(null, 4, null, 상품_18900원);
        final MemberCoupon memberCoupon = new MemberCoupon(1L, _20프로_할인_쿠폰);
        final Order order = Order.of(memberCoupon, 1L, List.of(cartItem1, cartItem2));

        // when
        final Money result = order.calculateDiscountPrice();

        // then
        assertThat(result).isEqualTo(Money.from(20460L));
    }

    @Test
    void 주문_상품의_할인_금액은_총_상품금액보다_클_수_없다() {
        // given
        final CartItem cartItem1 = new CartItem(null, 3, null, 상품_8900원);
        final CartItem cartItem2 = new CartItem(null, 4, null, 상품_18900원);
        final MemberCoupon memberCoupon = new MemberCoupon(1L, _20만원_할인_쿠폰);
        final Order order = Order.of(memberCoupon, 1L, List.of(cartItem1, cartItem2));

        // when
        final Money result = order.calculateDiscountPrice();

        // then
        assertThat(result).isEqualTo(Money.from(102300L));
    }

    @Test
    void 주문_상품의_배달비를_계산한다() {
        // given
        final CartItem cartItem1 = new CartItem(null, 3, null, 상품_18900원);
        final MemberCoupon memberCoupon = new MemberCoupon(1L, _3만원_이상_배달비_3천원_할인_쿠폰);
        final Order order = Order.of(memberCoupon, 1L, List.of(cartItem1));

        // when
        final Money result = order.calculateDeliveryFee();

        // then
        assertThat(result).isEqualTo(Money.ZERO);
    }
}
