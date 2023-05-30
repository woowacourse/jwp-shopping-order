package cart.domain;

import cart.domain.cart.CartItem;
import cart.domain.member.Member;
import cart.domain.order.OrderProducts;
import cart.domain.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderProductsTest {

    private static final double POINT_RATE = 0.05;
    private OrderProducts orderProducts;
    private Member member;

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

    @DisplayName("상품 가격의 총합을 계산한다.")
    @Test
    void calculateTotalAmount() {
        final int totalAmount = 11_000;
        assertThat(orderProducts.getTotalAmount()).isEqualTo(totalAmount);
    }

    @DisplayName("적립 포인트를 계산한다.")
    @Test
    void getSavedPoint() {
        final int savedPoint = (int) (orderProducts.getTotalAmount() * POINT_RATE);
        assertThat(orderProducts.getSavedPoint()).isEqualTo(savedPoint);
    }
}
