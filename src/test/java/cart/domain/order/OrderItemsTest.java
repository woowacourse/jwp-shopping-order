package cart.domain.order;

import static cart.fixtures.CartItemFixtures.바닐라_크림_콜드브루_ID_4_3개_17400원;
import static cart.fixtures.CartItemFixtures.유자_민트_티_ID_1_5개_29500원;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.cart.CartItem;
import cart.domain.cart.CartItems;
import cart.domain.Member;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderItemsTest {

    @DisplayName("주문 상품들의 합산 금액을 계산한다")
    @Test
    void sumOfPrice() {
        // given
        final Member member = new Member(1L, "test@email.com", "password");
        final List<CartItem> items = List.of(유자_민트_티_ID_1_5개_29500원(member), 바닐라_크림_콜드브루_ID_4_3개_17400원(member));
        final CartItems cartItems = new CartItems(items, member);
        final OrderItems orderItems = OrderItems.from(cartItems);

        // when
        Price sum = orderItems.sumOfPrice();

        // then
        assertThat(sum.getValue()).isEqualTo(46_900);
    }

}
