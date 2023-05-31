package cart.repository;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderItemEntity;
import cart.domain.Member;
import cart.domain.Money;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.exception.OrderException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public OrderRepository(final OrderDao orderDao, final OrderItemDao orderItemDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
    }

    public long add(final Order order) {
        final long orderId = orderDao.save(
                new OrderEntity(order.getMember().getId(), order.getDeliveryFee().getValue()));
        orderItemDao.saveAll(OrderItemEntity.of(orderId, order.getOrderItems()));
        return orderId;
    }

    public List<Order> findByMember(final Member member) {
        final List<OrderEntity> foundOrders = orderDao.findByMemberId(member.getId());
        final List<Order> orders = new ArrayList<>();
        for (final OrderEntity order : foundOrders) {
            final List<OrderItemEntity> foundOrderItems = orderItemDao.findByOrderId(order.getId());
            orders.add(new Order(order.getId(), OrderItem.from(foundOrderItems)));
        }
        return orders;
    }

    public Order findById(final Member member, final Long orderId) {
        final OrderEntity foundOrder = orderDao.find(member.getId(), orderId)
                .orElseThrow(() -> new OrderException.IllegalId(orderId));
        final List<OrderItemEntity> foundOrderItems = orderItemDao.findByOrderId(orderId);
        return new Order(foundOrder.getId(), OrderItem.from(foundOrderItems), new Money(foundOrder.getDeliveryFee()));
    }

    public void remove(final Member member, final Long orderId) {
        final OrderEntity order = orderDao.find(member.getId(), orderId)
                .orElseThrow(() -> new OrderException.IllegalId(orderId));
        orderDao.deleteById(orderId);
        orderItemDao.deleteByOrderId(orderId);
    }
}
