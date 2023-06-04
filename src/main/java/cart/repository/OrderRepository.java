package cart.repository;

import cart.dao.order.OrderCartItemDao;
import cart.dao.order.OrderDao;
import cart.domain.*;
import cart.domain.order.Order;
import cart.domain.order.OrderCartItem;
import cart.entity.OrderCartItemEntity;
import cart.entity.OrderEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepository {
    private final OrderDao orderDao;
    private final OrderCartItemDao orderCartItemDao;

    public OrderRepository(OrderDao orderDao, OrderCartItemDao orderCartItemDao) {
        this.orderDao = orderDao;
        this.orderCartItemDao = orderCartItemDao;
    }

    public Long insert(Member member, Order order) {
        Long orderId = orderDao.insert(OrderEntity.of(member, order));
        List<OrderCartItem> orderCartItems = order.getOrderCartItems();
        for (OrderCartItem orderCartItem : orderCartItems) {
            orderCartItemDao.insert(OrderCartItemEntity.of(orderId, orderCartItem));
        }
        return orderId;
    }

    public List<OrderEntity> findAllOrder(Member member) {
        return orderDao.findAll(member.getId());
    }

    public List<OrderCartItemEntity> findOrderItemsByOrderId(Long orderId) {
        return orderCartItemDao.findByOrderId(orderId);
    }

}
