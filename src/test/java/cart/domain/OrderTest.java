package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("Order 생성 성공")
    void create_success() {
        Member member = new Member(1L, "홍홍", "honghong");
        Product hongProduct = new Product("홍실", 1_000_000_000, "hong.com");
        CartItem cartItem = new CartItem(member, hongProduct);
        Order order = Order.of(member, List.of(cartItem));

        assertThat(order.getTimeStamp()).isNotNull();
        assertThat(order.getMember()).isEqualTo(member);
        assertThat(order.getOrderProducts()).hasSize(1)
                .extracting(OrderProduct::getProduct)
                .extracting(Product::getName, Product::getPrice, Product::getImageUrl)
                .contains(tuple("홍실", 1_000_000_000, "hong.com"));
    }

    @Test
    @DisplayName("Order 생성 실패 (장바구니를 담은 유저와 주문한 유저가 다름)")
    void create_fail() {
        Member member = new Member(1L, "홍홍", "honghong");
        Member illegalMember = new Member(2L, "홍실", "honghong");
        Product hongProduct = new Product("홍실", 1_000_000_000, "hong.com");
        CartItem cartItem = new CartItem(member, hongProduct);
        List<CartItem> cartItems = List.of(cartItem);

        assertThatThrownBy(() -> Order.of(illegalMember, cartItems))
                .isInstanceOf(IllegalArgumentException.class);
    }

}