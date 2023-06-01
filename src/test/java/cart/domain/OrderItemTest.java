package cart.domain;

import static cart.fixture.DomainFixture.CHICKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.OrderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OrderItemTest {

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    @DisplayName("OrderItem의 생성자를 호출할 때 {0}인 quantity를 전달하면 예외가 발생한다.")
    void constructorFailTest() {
        assertThatThrownBy(() -> new OrderItem(CHICKEN, 0))
                .isInstanceOf(OrderException.InvalidQuantity.class)
                .hasMessage("주문하고자 하는 상품의 수량은 1 이상이어야 합니다.");
    }

    @Test
    @DisplayName("calculateTotalPrice를 호출하면 주문한 상품의 가격과 수량을 곱한 결과를 반환한다.")
    void calculateTotalPriceTest() {
        OrderItem orderItem = new OrderItem(CHICKEN, 3);
        Money expected = new Money(30_000);

        assertThat(orderItem.calculateTotalPrice()).isEqualTo(expected);
    }
}
