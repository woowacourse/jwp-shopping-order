package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.IllegalOrderException;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("한 개 이상의 상품을 주문하지 않으면 주문에 실패한다.")
    void createOrder_empty_fail() {
        // when, then
        assertThatThrownBy(() -> new Order(Collections.emptyList()))
            .isInstanceOf(IllegalOrderException.class);
    }

    @Test
    @DisplayName("주문할 상품의 총 금액을 계산할 수 있다.")
    void calculateTotalOrderPrice() {
        // given
        List<OrderItem> orderItems = List.of(
            new OrderItem(new Product("치킨", Money.from(10_000), "tmpImg"), Quantity.from(2)),
            new OrderItem(new Product("피자", Money.from(15_000), "tmpImg"), Quantity.from(1))
        );
        Order order = new Order(orderItems);

        // when
        Money price = order.calculateTotalPrice();

        // then
        Money expected = Money.from(35_000);
        assertThat(price).isEqualTo(expected);
    }

}
