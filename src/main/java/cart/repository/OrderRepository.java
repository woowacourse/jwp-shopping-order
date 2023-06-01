package cart.repository;

import cart.dao.OrderItemDao;
import cart.dao.OrderDao;
import cart.dao.entity.OrderItemEntity;
import cart.domain.order.Order;
import cart.dao.entity.OrderEntity;
import cart.domain.order.OrderHistory;
import cart.exception.OrderException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public OrderRepository(OrderDao orderDao, OrderItemDao orderItemDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
    }

    public Long saveOrder(Order order) {
        Long saveOrderId = orderDao.insertOrder(OrderEntity.toEntity(order));
        orderItemDao.insertBatch(collectOrderItemEntities(order, saveOrderId));

        return saveOrderId;
    }

    private List<OrderItemEntity> collectOrderItemEntities(Order order, Long saveOrderId) {
        return order.getCartItems()
                .getCartItems()
                .stream()
                .map(cartItem -> OrderItemEntity.from(saveOrderId, cartItem))
                .collect(Collectors.toList());
    }

    public OrderHistory findByOrderId(Long orderId) {
        return orderDao.getByOrderId(orderId)
                .orElseThrow(OrderException.NotFound::new);
    }

    public List<OrderHistory> findOrdersByMemberId(Long memberId) {
        return orderDao.getAllByMemberId(memberId);
    }
}
