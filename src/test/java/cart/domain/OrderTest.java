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
        final Products products = new Products(List.of(
            new Product("name1", Amount.of(1_000), "url1"),
            new Product("name2", Amount.of(2_000), "url2")));
        final Coupon amountCoupon = new Coupon(1L, "name", Amount.of(1_000), Amount.of(1_000), false);
        final Order order = new Order(1L, products, amountCoupon, Amount.of(3000), "여기저기");

        //when
        final Amount discountedProductAmount = order.discountProductAmount();

        //then
        assertThat(discountedProductAmount).isEqualTo(Amount.of(2_000));
    }
}
