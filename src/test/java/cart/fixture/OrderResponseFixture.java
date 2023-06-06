package cart.fixture;

import cart.domain.Order;
import cart.dto.OrderResponse;

public class OrderResponseFixture {

    public static final OrderResponse ORDER_RESPONSE = OrderResponse.of(Order.of(10L, OrderFixture.ORDER1));

    public static final OrderResponse ORDER1_RESPONSE = OrderResponse.of(OrderFixture.ORDER1);

    public static final OrderResponse ORDER2_RESPONSE = OrderResponse.of(OrderFixture.ORDER2);
}
