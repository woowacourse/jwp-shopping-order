package cart.domain.order;

import static cart.fixture.CouponFixture._20만원_할인_쿠폰;
import static cart.fixture.CouponFixture._20프로_할인_쿠폰;
import static cart.fixture.CouponFixture._3만원_이상_배달비_3천원_할인_쿠폰;
import static cart.fixture.OrderItemFixture.상품_18900원_주문;
import static cart.fixture.OrderItemFixture.상품_28900원_1개_주문;
import static cart.fixture.OrderItemFixture.상품_8900원_주문;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.domain.VO.Money;
import cart.exception.order.InvalidOrderException;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderTest {

    @Test
    void 주문_생성_시_쿠폰을_적용할_수_없는_경우_예외를_던진다() {
        assertThatThrownBy(() -> Order.of(_3만원_이상_배달비_3천원_할인_쿠폰, 1L, List.of(상품_28900원_1개_주문)))
                .isInstanceOf(InvalidOrderException.class)
                .hasMessage("쿠폰을 적용할 수 없는 주문입니다.");
    }

    @Test
    void 주문한_사용자가_아니라면_예외를_던진다() {
        final Order order = Order.of(_20프로_할인_쿠폰, 1L, List.of(상품_28900원_1개_주문));

        assertThatThrownBy(() -> order.checkOwner(Long.MAX_VALUE))
                .isInstanceOf(InvalidOrderException.class)
                .hasMessage("주문의 소유자가 아닙니다.");
    }

    @Test
    void 주문_상품의_총합을_계산한다() {
        // given
        final Order order = Order.of(null, 1L, List.of(상품_8900원_주문(3), 상품_18900원_주문(4)));

        // when
        final Money result = order.calculateTotalPrice();

        // then
        assertThat(result).isEqualTo(Money.from(102300L));
    }

    @Test
    void 주문_상품의_할인_금액을_계산한다() {
        // given
        final Order order = Order.of(_20프로_할인_쿠폰, 1L, List.of(상품_8900원_주문(3), 상품_18900원_주문(4)));

        // when
        final Money result = order.calculateDiscountPrice();

        // then
        assertThat(result).isEqualTo(Money.from(20460L));
    }

    @Test
    void 주문_상품의_할인_금액은_총_상품금액보다_클_수_없다() {
        // given
        final Order order = Order.of(_20만원_할인_쿠폰, 1L, List.of(상품_8900원_주문(3), 상품_18900원_주문(4)));

        // when
        final Money result = order.calculateDiscountPrice();

        // then
        assertThat(result).isEqualTo(Money.from(102300L));
    }

    @Test
    void 주문_상품의_배달비를_계산한다() {
        // given
        final Order order = Order.of(_3만원_이상_배달비_3천원_할인_쿠폰, 1L, List.of(상품_18900원_주문(3)));

        // when
        final Money result = order.calculateDeliveryFee();

        // then
        assertThat(result).isEqualTo(Money.ZERO);
    }

    @Test
    void 쿠폰을_사용하면_쿠폰_사용_완료_상태가_된다() {
        // given
        final Order order = Order.of(_3만원_이상_배달비_3천원_할인_쿠폰, 1L, List.of(상품_18900원_주문(3)));

        // when
        order.useCoupon();

        // then
        assertThat(order.getCoupon().isUsed()).isEqualTo(true);
    }
}
