package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.domain.delivery.AdvancedDeliveryPolicy;
import cart.domain.discount.AdvancedDiscountPolicy;
import cart.domain.order.Order;
import cart.exception.CartItemException.CartItemNotExistException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CartItemsTest {

    @DisplayName("장바구니에 존재하는 상품을 구매한다.")
    @Test
    void buy() {
        //given
        final Member member = new Member(null, "email", "password");
        final Product product = new Product(1L, "product", 1000, "imageUrl");
        final CartItem cartItem = new CartItem(1L, 1, product, member);
        final CartItems cartItems = new CartItems(List.of(cartItem));

        //when
        //then
        assertDoesNotThrow(() -> cartItems.buy(1L, 1));
    }

    @DisplayName("장바구니에 존재하지 않는 상품을 구매시 예외가 발생한다.")
    @Test
    void buy_fail() {
        //given
        final Member member = new Member(null, "email", "password");
        final Product product = new Product(1L, "product", 1000, "imageUrl");
        final CartItem cartItem = new CartItem(1L, 1, product, member);
        final CartItems cartItems = new CartItems(List.of(cartItem));

        //when
        //then
        assertThatThrownBy(() -> cartItems.buy(2L, 1))
            .isInstanceOf(CartItemNotExistException.class);
    }

    @DisplayName("구매하는 상품들을 포함한 주문을 생성한다.")
    @Test
    void order() {
        //given
        final Member member = new Member(null, "email", "password");
        final Product product = new Product(1L, "product", 1000, "imageUrl");
        final CartItem cartItem = new CartItem(1L, 1, product, member);
        final CartItems cartItems = new CartItems(List.of(cartItem));
        cartItems.buy(1L, 1);

        //when
        final Order order = cartItems.order(member, LocalDateTime.now(), new AdvancedDiscountPolicy(),
            new AdvancedDeliveryPolicy());

        //then
        assertAll(
            () -> assertThat(order.getMember()).isEqualTo(member),
            () -> assertThat(order.getOrderItems()).hasSize(1),
            () -> assertThat(order.getProductPrice()).isEqualTo(1000),
            () -> assertThat(order.getOrderPrice().getDiscountPrice()).isEqualTo(0),
            () -> assertThat(order.getOrderPrice().getDeliveryFee()).isEqualTo(3000),
            () -> assertThat(order.getOrderPrice().getTotalPrice()).isEqualTo(4000)
        );
    }
}
