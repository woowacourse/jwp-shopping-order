package cart.repository;

import cart.dao.OrderDao;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.entity.OrderDetailEntity;
import cart.entity.OrderEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;

    public OrderRepository(final OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public Long addOrder(final Order order) {
        final Long orderId = orderDao.addOrder(new OrderEntity(order.getMemberId(), order.getPaymentAmount(), order.getPointAmount()));

        final List<OrderItem> orderItems = order.getOrderItems();
        orderItems.stream()
                .map(orderItem -> new OrderDetailEntity(orderId, orderItem.getProductId(), orderItem.getQuantity()))
                .forEach(orderDao::addOrderDetail);

        return orderId;
    }

//    public Order getOrder(final )
}
