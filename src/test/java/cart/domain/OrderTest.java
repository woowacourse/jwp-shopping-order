package cart.domain;

import static cart.TestDataFixture.MEMBER_1;
import static cart.TestDataFixture.MEMBER_2;
import static cart.TestDataFixture.PRODUCT_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.domain.product.CartItem;
import cart.domain.product.Product;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("Order 생성 성공")
    void create_success() {
        final Member member = new Member(1L, "홍홍", "honghong");
        final Product hongProduct = new Product("홍실", 1_000_000_000, "hong.com");
        final CartItem cartItem = new CartItem(member, hongProduct);
        final Order order = Order.of(member, List.of(cartItem), null);

        assertThat(order.getTimeStamp()).isNotNull();
        assertThat(order.getMemberId()).isEqualTo(member.getId());
        assertThat(order.getOrderProducts()).hasSize(1)
                .extracting(OrderProduct::getProduct)
                .extracting(Product::getName, Product::getPrice, Product::getImageUrl)
                .contains(tuple("홍실", 1_000_000_000, "hong.com"));
    }

    @Test
    @DisplayName("Order 생성 실패 (장바구니를 담은 유저와 주문한 유저가 다름)")
    void create_fail() {
        final CartItem cartItem = new CartItem(MEMBER_1, PRODUCT_1);
        final List<CartItem> cartItems = List.of(cartItem);

        assertThatThrownBy(() -> Order.of(MEMBER_2, cartItems, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
