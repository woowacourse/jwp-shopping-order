package cart.domain.order;

import cart.TestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderProductsTest {

    private OrderProducts orderProducts;

    @BeforeEach
    void setUp() {
        orderProducts = new OrderProducts(
                List.of(
                        TestFixture.getCartItem1(),
                        TestFixture.getCartItem2()
                )
        );
    }

    @DisplayName("상품 가격의 총합을 계산한다.")
    @Test
    void calculateTotalAmount() {
        final int totalAmount = 100_000;
        assertThat(orderProducts.getTotalAmount()).isEqualTo(totalAmount);
    }

    @DisplayName("적립 포인트를 계산한다.")
    @Test
    void getSavedPoint() {
        final double pointRate = 0.05;
        final int totalAmount = 100_000;
        final int savedPoint = (int) (totalAmount * pointRate);
        assertThat(orderProducts.getSavedPoint()).isEqualTo(savedPoint);
    }
}
