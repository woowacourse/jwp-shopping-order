package cart.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.domain.product.Product;
import cart.exception.OrderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OrderProductTest {

    @Nested
    @DisplayName("주문 상품 생성 시 ")
    class Create {

        @ParameterizedTest
        @ValueSource(ints = {Integer.MIN_VALUE, -1, 0})
        @DisplayName("수량이 0보다 작거나 같은 경우 예외를 던진다.")
        void invalidQuantity(int quantity) {
            Product product = new Product("치킨", 10000, "http://chicken.com");

            assertThatThrownBy(() -> new OrderProduct(product, quantity))
                    .isInstanceOf(OrderException.class)
                    .hasMessage("주문 상품 수량은 최소 1개부터 가능합니다. 현재 개수: " + quantity);
        }
    }

    @Test
    @DisplayName("calculateTotalPrice 메서드는 주문 상품에 대한 합계를 계산한다.")
    void calculateTotalPrice() {
        Product product = new Product("치킨", 10000, "http://chicken.com");
        OrderProduct orderProduct = new OrderProduct(product, 5);

        int result = orderProduct.calculateTotalPrice();

        assertThat(result).isEqualTo(50000);
    }
}
