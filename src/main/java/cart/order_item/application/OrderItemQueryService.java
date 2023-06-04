package cart.order_item.application;

import cart.order.domain.Order;
import cart.order_item.dao.OrderItemDao;
import cart.order_item.domain.OrderItem;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderItemQueryService {

  private final OrderItemDao orderItemDao;

  public OrderItemQueryService(final OrderItemDao orderItemDao) {
    this.orderItemDao = orderItemDao;
  }

  // TODO : OrderItem 을 사용하지 않고 무조건 OrderedItem 사용하도록
  public List<OrderItem> searchOrderItemsByOrderId(final Order order) {
    return orderItemDao.findByOrderId(order.getId());
  }
}