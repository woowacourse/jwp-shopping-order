package cart.repository;

import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderItemEntity;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OrderDbRepository implements OrderRepository {

    private final MemberDao memberDao;
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public OrderDbRepository(final MemberDao memberDao, final OrderDao orderDao,
        final OrderItemDao orderItemDao) {
        this.memberDao = memberDao;
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
    }

    @Override
    public Long create(final Order order) {
        final long orderId = this.orderDao.insert(OrderEntity.from(order));
        final List<OrderItem> orderItems = order.getOrderItems();
        orderItems.forEach(
            orderItem -> this.orderItemDao.insert(OrderItemEntity.from(orderItem, orderId)));

        return orderId;
    }

    @Override
    public List<Order> findByMember(final Member member) {
        final List<OrderEntity> orderEntities = this.orderDao.findByMemberId(member.getId());

        return orderEntities.stream()
            .map(orderEntity -> this.toOrder(member, orderEntity))
            .collect(Collectors.toList());
    }

    private Order toOrder(final Member member, final OrderEntity orderEntity) {
        final List<OrderItem> orderItems = this.orderItemDao.findByOrderId(orderEntity.getId())
            .stream()
            .map(OrderItemEntity::toOrderItem)
            .collect(Collectors.toList());

        return orderEntity.toOrder(member, orderItems);
    }

    @Override
    public Optional<Order> findById(final Long id) {
        try {
            final OrderEntity orderEntity = this.orderDao.findById(id);
            final Member member = this.memberDao.getMemberById(orderEntity.getMemberId());

            return Optional.of(this.toOrder(member, orderEntity));
        } catch (final DataAccessException exception) {
            return Optional.empty();
        }
    }
}
