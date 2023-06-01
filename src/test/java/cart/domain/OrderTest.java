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
        final CartItem cartItem = new CartItem(MEMBER_1, PRODUCT_1);
        final Order order = Order.of(MEMBER_1, List.of(cartItem));

        assertThat(order.getTimeStamp()).isNotNull();
        assertThat(order.getMemberId()).isEqualTo(MEMBER_1.getId());
        assertThat(order.getOrderProducts()).hasSize(1)
                .extracting(OrderProduct::getProduct)
                .extracting(Product::getName, Product::getPrice, Product::getImageUrl)
                .contains(tuple(PRODUCT_1.getName(), PRODUCT_1.getPrice(), PRODUCT_1.getImageUrl()));
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
