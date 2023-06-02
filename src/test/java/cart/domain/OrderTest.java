package cart.domain;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.DiscountType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class OrderTest {


    @Test
    @DisplayName("주문시 카트 상품이 비어있지 않게 생성한다.")
    void createOrder() {
        Member member = new Member(1L, "ocean@ocena", "1234");
        Product product = new Product("오션", 10000, "ocean");
        CartItem cartItem = new CartItem(member, product);

        assertDoesNotThrow(() -> new Order(member, List.of(cartItem), null));
    }

    @Test
    @DisplayName("주문시 카트 상품이 비어있으면 예외가 발생한다.")
    void createOrderException() {
        Member member = new Member(1L, "ocean@ocean", "1234");

        assertThatThrownBy(() -> new Order(member, List.of(), null))
                .hasMessage("주문상품이 비어있습니다.");
    }

    @Test
    @DisplayName("할인 전 가격을 계산할 수 있다.")
    void calculatePrice() {
        Member member = new Member(1L, "ocean@ocena", "1234");
        Product product = new Product("오션", 10000, "ocean");
        CartItem cartItem = new CartItem(member, product);
        Order order = new Order(member, List.of(cartItem), null);
        assertThat(order.calculatePrice()).isEqualTo(10000);
    }

    @Test
    @DisplayName("할인 후 가격을 계산할 수 있다.")
    void calculateDiscountPrice() {
        Member member = new Member(1L, "ocean@ocena", "1234");
        Product product = new Product("오션", 10000, "ocean");
        CartItem cartItem = new CartItem(member, product);
        Coupon coupon = new Coupon("50%할인쿠폰", DiscountType.percentDiscount.getType(), 0, 0, 0.5);
        Order order = new Order(member, List.of(cartItem), coupon);
        assertThat(order.calculateDiscountPrice()).isEqualTo(5000);
    }
}
