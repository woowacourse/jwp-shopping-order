package cart.domain.order;

import cart.domain.cart.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    private Member member;
    private OrderProducts orderProducts;

    @BeforeEach
    void setUp() {
        member = new Member(1L, "a@a.com", "1234", 100);
        final Product product1 = new Product(1L, "name1", 1_000, "imageUrl1");
        final Product product2 = new Product(2L, "name2", 5_000, "imageUrl2");
        final List<CartItem> cartItems = List.of(
                new CartItem(1L, member, product1, 1),
                new CartItem(1L, member, product2, 2)
        );
        orderProducts = new OrderProducts(cartItems);
    }

    @DisplayName("적립금을 사용할 경우 총 가격에서 적립금을 뺀 가격만 결제한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 100, 1_000})
    void getTotalAmountTest(final int usingPoint) {
        final Order order = new Order(member, usingPoint, orderProducts);

        assertThat(order.getTotalAmount()).isEqualTo(orderProducts.getTotalAmount() - usingPoint);
    }
}
