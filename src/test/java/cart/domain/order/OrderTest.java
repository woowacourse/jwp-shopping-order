package cart.domain.order;

import static cart.TestDataFixture.DISCOUNT_50_PERCENT;
import static cart.TestDataFixture.MEMBER_1;
import static cart.domain.DomainTestDataFixture.CART_ITEM_1;
import static cart.domain.DomainTestDataFixture.CART_ITEM_2;
import static cart.domain.DomainTestDataFixture.ORDER_NO_USE_COUPON;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.coupon.MemberCoupon;
import cart.domain.vo.Price;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderTest {

    @DisplayName("주문의 상품들의 가격을 전부 계산하는 기능 테스트")
    @Test
    void getOriginPrice() {
        final Price originPrice = ORDER_NO_USE_COUPON.getOrderProducts().stream()
                .map(OrderProduct::calculateTotalPrice)
                .reduce(new Price(0), Price::sum);

        final Price price = ORDER_NO_USE_COUPON.getOriginPrice();

        assertThat(price).isEqualTo(originPrice);
    }

    @DisplayName("할인 금액을 계산하는 기능 테스트")
    @Nested
    class getDiscountPrice {

        @DisplayName("쿠폰을 사용한 주문인 경우 할인금액이 반환된다.")
        @Test
        void haveCoupon() {
            final MemberCoupon memberCoupon = new MemberCoupon(DISCOUNT_50_PERCENT, MEMBER_1.getId());
            final Order order = Order.of(MEMBER_1, List.of(CART_ITEM_1, CART_ITEM_2), memberCoupon);
            final Price originPrice = order.getOrderProducts().stream()
                    .map(OrderProduct::calculateTotalPrice)
                    .reduce(new Price(0), Price::sum);
            final Price expectedDiscountPrice = memberCoupon.discount(originPrice);

            final Price discountPrice = order.getDiscountPrice();

            assertThat(discountPrice)
                    .isEqualTo(expectedDiscountPrice);
        }

        @DisplayName("쿠폰을 사용하지 않은 주문인 경우 0이 반환된다.")
        @Test
        void notHaveCoupon() {
            final Price discountPrice = ORDER_NO_USE_COUPON.getDiscountPrice();

            assertThat(discountPrice)
                    .isEqualTo(new Price(0));
        }

    }

    @DisplayName("총 가격을 계산하는 기능 테스트")
    @Test
    void getTotalPrice() {
        final MemberCoupon memberCoupon = new MemberCoupon(DISCOUNT_50_PERCENT, MEMBER_1.getId());
        final Order order = Order.of(MEMBER_1, List.of(CART_ITEM_1, CART_ITEM_2), memberCoupon);
        final Price originPrice = order.getOrderProducts().stream()
                .map(OrderProduct::calculateTotalPrice)
                .reduce(new Price(0), Price::sum);
        final Price discountPrice = memberCoupon.discount(originPrice);
        final Price expectedTotalPrice = originPrice.subtract(discountPrice);

        final Price totalPrice = order.getTotalPrice();

        assertThat(totalPrice)
                .isEqualTo(expectedTotalPrice);
    }
}
