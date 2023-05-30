package cart.order_item.application;

import cart.order.domain.Order;
import cart.order_item.dao.OrderItemDao;
import cart.order_item.domain.OrderItem;
import cart.value_object.Money;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderItemQueryService {

  private final OrderItemDao orderItemDao;

  public OrderItemQueryService(final OrderItemDao orderItemDao) {
    this.orderItemDao = orderItemDao;
  }

  public List<OrderItem> searchOrderItemsByOrderId(final Order order) {
    return orderItemDao.findByOrderId(order.getId())
        .stream()
        .map(it -> new OrderItem(
            it.getId(), order,
            it.getName(), new Money(it.getPrice()),
            it.getImageUrl(), it.getQuantity()))
        .collect(Collectors.toList());
  }
}
