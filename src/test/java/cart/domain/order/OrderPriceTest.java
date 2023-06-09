package cart.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.delivery.DeliveryPolicy;
import cart.domain.discount.DiscountPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderPriceTest {

    @DisplayName("상품가격과 할인 정책, 배송비 측정 정책으로 주문 가격을 생성한다.")
    @Test
    void of() {
        //given
        //when
        final OrderPrice orderPrice = OrderPrice.of(1000L, new TestDiscount(), new TestDelivery());

        //then
        assertAll(
            () -> assertThat(orderPrice.getProductPrice()).isEqualTo(1000L),
            () -> assertThat(orderPrice.getDiscountPrice()).isEqualTo(0L),
            () -> assertThat(orderPrice.getDeliveryFee()).isEqualTo(0L),
            () -> assertThat(orderPrice.getTotalPrice()).isEqualTo(1000L)
        );
    }

    private static class TestDiscount implements DiscountPolicy {

        @Override
        public Long calculate(final Long price) {
            return 0L;
        }
    }

    private static class TestDelivery implements DeliveryPolicy {

        @Override
        public Long getDeliveryFee(final Long productPrice) {
            return 0L;
        }
    }
}
