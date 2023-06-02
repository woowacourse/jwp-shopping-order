package cart.repository;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderItemEntity;
import cart.domain.Member;
import cart.domain.Order;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public OrderRepository(final OrderDao orderDao, final OrderItemDao orderItemDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
    }

    public long add(final Order order) {
        final long orderId = orderDao.save(OrderEntity.from(order));
        orderItemDao.saveAll(OrderItemEntity.of(orderId, order.getOrderItems()));
        return orderId;
    }

    public List<Order> findByMember(final Member member) {
        final List<OrderEntity> foundOrders = orderDao.findByMemberId(member.getId());
        final List<Order> orders = new ArrayList<>();
        for (final OrderEntity foundOrder : foundOrders) {
            final List<OrderItemEntity> foundOrderItems = orderItemDao.findByOrderId(foundOrder.getId());
            orders.add(Order.from(foundOrder, foundOrderItems));
        }
        return orders;
    }

    public Optional<Order> findById(final Long orderId) {
        final Optional<OrderEntity> foundResult = orderDao.findById(orderId);
        if (foundResult.isEmpty()) {
            return Optional.empty();
        }
        final OrderEntity foundOrder = foundResult.get();
        final List<OrderItemEntity> foundOrderItems = orderItemDao.findByOrderId(orderId);
        return Optional.of(Order.from(foundOrder, foundOrderItems));
    }

    public void remove(final Long orderId) {
        orderDao.deleteById(orderId);
        orderItemDao.deleteByOrderId(orderId);
    }

    public void update(final Order order) {
        orderDao.updateStatus(OrderEntity.from(order));
    }
}
