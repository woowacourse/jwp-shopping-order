package cart.repository;

import cart.dao.MemberDao;
import cart.dao.OrderItemDao;
import cart.dao.OrderRecordDao;
import cart.dao.entity.OrderItemEntity;
import cart.dao.entity.OrderRecordEntity;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.exception.AuthenticationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDbRepository implements OrderRepository {

    private final MemberDao memberDao;
    private final OrderRecordDao orderRecordDao;
    private final OrderItemDao orderItemDao;

    public OrderDbRepository(final MemberDao memberDao, final OrderRecordDao orderRecordDao,
                             final OrderItemDao orderItemDao) {
        this.memberDao = memberDao;
        this.orderRecordDao = orderRecordDao;
        this.orderItemDao = orderItemDao;
    }

    @Override
    public Long create(final Order order) {
        final long orderId = this.orderRecordDao.insert(OrderRecordEntity.from(order));

        final List<OrderItem> orderItems = order.getOrderItems();
        orderItems.forEach(orderItem -> this.orderItemDao.insert(OrderItemEntity.from(orderItem, orderId)));

        return orderId;
    }

    @Override
    public List<Order> findByMember(final Member member) {
        final List<OrderRecordEntity> orderEntities = this.orderRecordDao.findByMemberId(member.getId());

        return orderEntities.stream()
                .map(orderRecordEntity -> this.toOrder(member, orderRecordEntity))
                .collect(Collectors.toList());
    }

    private Order toOrder(final Member member, final OrderRecordEntity orderRecordEntity) {
        final List<OrderItem> orderItems = this.orderItemDao.findByOrderId(orderRecordEntity.getId()).stream()
                .map(OrderItemEntity::toOrderItem)
                .collect(Collectors.toList());

        return orderRecordEntity.toOrder(member, orderItems);
    }

    @Override
    public Optional<Order> findById(final Long id) {
        try {
            final OrderRecordEntity orderRecordEntity = this.orderRecordDao.findById(id);
            final Member member = this.memberDao.getMemberById(orderRecordEntity.getMemberId())
                    .orElseThrow(AuthenticationException.NotFound::new);
            return Optional.of(this.toOrder(member, orderRecordEntity));
        } catch (final DataAccessException exception) {
            return Optional.empty();
        }
    }
}
