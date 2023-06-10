package cart.fixture;

import cart.dto.order.OrderRequest;
import java.util.List;

public class OrderRequestFixture {
    public static final OrderRequest ORDER_REQUEST_1 = new OrderRequest(List.of(1L, 2L, 3L), 8000);
}
