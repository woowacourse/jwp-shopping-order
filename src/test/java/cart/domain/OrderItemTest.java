package cart.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Order Item 단위 테스트")
public class OrderItemTest {

    @Test
    @DisplayName("개별 주문 상품 가격 계산 테스트")
    void calculateTotalPrice_test() {
        // given
        final Product product = new Product(1L, "양갈비", new Price(25000), "양갈비.jpg");
        final OrderItem orderItem = new OrderItem(product, new Quantity(3));

        // when
        final Price totalPrice = orderItem.calculateTotalPrice();

        // then
        final Price expected = new Price(75000);
        assertThat(totalPrice).isEqualTo(expected);
    }
}
