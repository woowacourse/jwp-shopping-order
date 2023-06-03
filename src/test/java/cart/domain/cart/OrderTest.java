package cart.domain.cart;

import static cart.fixture.CouponFixture._20만원_할인_쿠폰;
import static cart.fixture.CouponFixture._20프로_할인_쿠폰;
import static cart.fixture.CouponFixture._3만원_이상_배달비_3천원_할인_쿠폰;
import static cart.fixture.ProductFixture.상품_18900원;
import static cart.fixture.ProductFixture.상품_8900원;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.domain.VO.Money;
import cart.exception.cart.InvalidCartItemOwnerException;
import cart.exception.cart.InvalidOrderException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderTest {

    @Test
    void 주문_생성_시_쿠폰을_적용할_수_없는_경우_예외를_던진다() {
        final MemberCoupon memberCoupon = new MemberCoupon(1L, _3만원_이상_배달비_3천원_할인_쿠폰);
        final CartItem cartItem = new CartItem(1L, 3, 1L, 상품_8900원);
        assertThatThrownBy(() -> Order.of(memberCoupon, 1L, of(cartItem)))
                .isInstanceOf(InvalidOrderException.class)
                .hasMessage("쿠폰을 적용할 수 없는 주문입니다.");
    }

    @Test
    void 주문_생성_시_카드에_담긴_상품_목록의_소유자가_아니라면_예외를_던진다() {
        final MemberCoupon memberCoupon = new MemberCoupon(1L, _20프로_할인_쿠폰);
        final CartItem cartItem = new CartItem(1L, 3, 2L, 상품_8900원);
        assertThatThrownBy(() -> Order.of(memberCoupon, 1L, of(cartItem)))
                .isInstanceOf(InvalidCartItemOwnerException.class)
                .hasMessage("장바구니의 소유자가 아닙니다.");
    }

    @Test
    void 주문한_사용자가_아니라면_예외를_던진다() {
        final MemberCoupon memberCoupon = new MemberCoupon(1L, _20프로_할인_쿠폰);
        final CartItem cartItem = new CartItem(1L, 3, 2L, 상품_8900원);
        final Order order = Order.of(memberCoupon, 1L, of(cartItem));

        assertThatThrownBy(() -> order.checkOwner(Long.MAX_VALUE))
                .isInstanceOf(InvalidOrderException.class)
                .hasMessage("주문의 소유자가 아닙니다.");
    }

    @Test
    void 주문_상품의_총합을_계산한다() {
        // given
        final CartItem cartItem1 = new CartItem(1L, 3, 1L, 상품_8900원);
        final CartItem cartItem2 = new CartItem(1L, 4, 1L, 상품_18900원);
        final Order order = Order.of(null, 1L, of(cartItem1, cartItem2));

        // when
        final Money result = order.calculateTotalPrice();

        // then
        assertThat(result).isEqualTo(Money.from(102300L));
    }

    @Test
    void 주문_상품의_할인_금액을_계산한다() {
        // given
        final CartItem cartItem1 = new CartItem(1L, 3, 1L, 상품_8900원);
        final CartItem cartItem2 = new CartItem(1L, 4, 1L, 상품_18900원);
        final MemberCoupon memberCoupon = new MemberCoupon(1L, _20프로_할인_쿠폰);
        final Order order = Order.of(memberCoupon, 1L, of(cartItem1, cartItem2));

        // when
        final Money result = order.calculateDiscountPrice();

        // then
        assertThat(result).isEqualTo(Money.from(20460L));
    }

    @Test
    void 주문_상품의_할인_금액은_총_상품금액보다_클_수_없다() {
        // given
        final CartItem cartItem1 = new CartItem(1L, 3, 1L, 상품_8900원);
        final CartItem cartItem2 = new CartItem(1L, 4, 1L, 상품_18900원);
        final MemberCoupon memberCoupon = new MemberCoupon(1L, _20만원_할인_쿠폰);
        final Order order = Order.of(memberCoupon, 1L, of(cartItem1, cartItem2));

        // when
        final Money result = order.calculateDiscountPrice();

        // then
        assertThat(result).isEqualTo(Money.from(102300L));
    }

    @Test
    void 주문_상품의_배달비를_계산한다() {
        // given
        final CartItem cartItem1 = new CartItem(1L, 3, 1L, 상품_18900원);
        final MemberCoupon memberCoupon = new MemberCoupon(1L, _3만원_이상_배달비_3천원_할인_쿠폰);
        final Order order = Order.of(memberCoupon, 1L, of(cartItem1));

        // when
        final Money result = order.calculateDeliveryFee();

        // then
        assertThat(result).isEqualTo(Money.ZERO);
    }

    @Test
    void 쿠폰을_사용하면_쿠폰_사용_완료_상태가_된다() {
        // given
        final CartItem cartItem = new CartItem(1L, 3, 1L, 상품_18900원);
        final MemberCoupon memberCoupon = new MemberCoupon(1L, _3만원_이상_배달비_3천원_할인_쿠폰);
        final Order order = Order.of(memberCoupon, 1L, of(cartItem));

        // when
        order.useCoupon();

        // then
        assertThat(memberCoupon.isUsed()).isEqualTo(true);
    }
}
