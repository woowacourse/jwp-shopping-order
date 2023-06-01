package cart.fixture;

import cart.domain.Order;
import cart.dto.OrderResponse;

public class OrderResponseFixture {

    public static final OrderResponse ORDER_RESPONSE = OrderResponse.of(new Order(10L, OrderFixture.ORDER));
}
