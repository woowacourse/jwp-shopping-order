package cart.order_item.application;

import cart.order.domain.Order;
import cart.order_item.application.mapper.OrderItemMapper;
import cart.order_item.dao.OrderItemDao;
import cart.order_item.dao.entity.OrderItemEntity;
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

  public List<OrderItem> searchOrderItemsByOrderId(final Order order) {
    final List<OrderItemEntity> orderItemEntities = orderItemDao.findByOrderId(order.getId());

    return OrderItemMapper.mapToOrderItems(orderItemEntities, order);
  }
}
