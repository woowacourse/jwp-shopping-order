package cart.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;

import java.util.List;

import static cart.ProductFixture.product1;
import static cart.ProductFixture.product2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @DisplayName("주문을 하면 전체 금액에서 사용한 포인트를 뺀 금액만큼에 대한 적립 금액을 구할 수 있다.")
    @Test
    void calculateSavedPoint() {
        List<OrderItem> orderItems = List.of(new OrderItem(product1, 3, 30000),
                new OrderItem(product2, 2, 40000));
        Order order = new Order(10000, orderItems, new Member(1L, "kong@com", "1234"));

        Point point = order.calculateSavedPoint(new OrderPointAccumulationPolicy(new OrderPointExpirePolicy()));

        assertThat(point.getValue()).isEqualTo(4800);
    }
}
