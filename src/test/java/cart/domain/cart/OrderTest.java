package cart.domain.cart;

import cart.domain.coupon.Coupons;
import cart.domain.member.Member;
import cart.dto.order.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static cart.fixture.CartFixture.createCart;
import static cart.fixture.CouponFixture.createCouponsWithDeliveryFree;
import static cart.fixture.MemberFixture.createMember;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderTest {

    /**
     * 1L, 치킨, 20000, false, 0%
     * 2L, 피자, 10000, true, 10%
     * <p>
     * 치킨 수량 1
     * 피자 수량 2
     * <p>
     * 1L, "1000원 할인 쿠폰", 1000
     * 2L, "10% 할인 쿠폰, 10
     */

    @DisplayName("장바구니에 상품을 계산한다.")
    @Test
    void pay_success() {
        // given
        Cart cart = createCart();
        Member member = createMember();
        Coupons coupons = createCouponsWithDeliveryFree();
        member.initCoupons(coupons);

        Order order = new Order(member, cart);
        List<Long> productIds = List.of(1L, 2L);
        List<Integer> quantities = List.of(1, 2);
        List<Long> couponIds = List.of(1L, 2L, 3L);

        // when
        OrderResponse pay = order.pay(productIds, quantities, couponIds);

        // then
        assertAll(
                () -> assertThat(pay.getDeliveryPrice().getDeliveryPrice()).isEqualTo(0),
                () -> assertThat(pay.getProducts().get(0).getProductName()).isEqualTo("치킨"),
                () -> assertThat(pay.getProducts().get(0).getPrice()).isEqualTo(17100),
                () -> assertThat(pay.getProducts().get(1).getPrice()).isEqualTo(15300)
        );
    }
}
