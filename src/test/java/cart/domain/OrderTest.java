package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.vo.Amount;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("상품 금액을 할인 적용한다.")
    void testDiscountProductAmount() {
        //given
        final List<OrderItem> orderItems = List.of(
                new OrderItem(new Product("name1", Amount.of(1_000), "url1"), 1),
                new OrderItem(new Product("name2", Amount.of(2_000), "url2"), 1)
        );
        final Coupon amountCoupon = new Coupon(1L, "name", Amount.of(1_000), Amount.of(1_000));
        final MemberCoupon memberCoupon = new MemberCoupon(1L, 1L, amountCoupon, false);
        final Order order = new Order(1L, orderItems, memberCoupon, Amount.of(3_000), Amount.of(2_000), "여기저기");

        //when
        final Amount discountedProductAmount = order.discountProductAmount();

        //then
        assertThat(discountedProductAmount).isEqualTo(Amount.of(2_000));
    }
}
