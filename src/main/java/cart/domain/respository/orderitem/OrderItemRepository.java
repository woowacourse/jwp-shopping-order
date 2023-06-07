package cart.domain.respository.orderitem;

import cart.domain.order.OrderItem;
import java.util.List;

public interface OrderItemRepository {

    OrderItem insert(final Long orderId, final OrderItem orderItem);

    void insertAll(final Long orderId, final List<OrderItem> orderItem);
}
