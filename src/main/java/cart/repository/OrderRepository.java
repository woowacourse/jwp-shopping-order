package cart.repository;

import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.order.OrderItems;
import cart.entity.MemberEntity;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import cart.exception.MemberNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final MemberDao memberDao;

    public OrderRepository(final OrderDao orderDao, final OrderItemDao orderItemDao, final MemberDao memberDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.memberDao = memberDao;
    }

    public Order save(final Order order) {
        final OrderEntity orderEntity = OrderEntity.from(order);
        if (Objects.isNull(order.getId())) {
            final OrderEntity entity = orderDao.insert(orderEntity);
            final List<OrderItemEntity> orderItemEntities = mapToOrderItemEntities(order, entity.getId());
            orderItemEntities.forEach(orderItemDao::insert);

            return new Order(entity.getId(), order.getOrderItems(), order.getDeliveryFee(), order.getMemberCouponId(), order.getMemberId());
        }
        orderDao.update(orderEntity);
        final List<OrderItemEntity> orderItemEntities = mapToOrderItemEntities(order, order.getId());
        orderItemEntities.forEach(orderItemDao::update);
        return order;
    }

    private List<OrderItemEntity> mapToOrderItemEntities(final Order order, final Long orderId) {
        return order.getOrderItems().getOrderItems().stream()
                .map(item -> OrderItemEntity.of(item, orderId))
                .collect(Collectors.toList());
    }

    public List<Order> findByMemberId(final Long memberId) {
        final List<OrderEntity> orderEntities = orderDao.findByMemberId(memberId);
        if (orderEntities.size() == 0) {
            return Collections.emptyList();
        }

        return orderEntities.stream()
                .map(this::mapToOrder)
                .collect(Collectors.toList());
    }

    private Order mapToOrder(final OrderEntity orderEntity) {
        final List<OrderItem> orderItems = getOrderItems(orderEntity.getId());
        final Member member = getMember(orderEntity.getMemberId());

        return new Order(orderEntity.getId(), new OrderItems(orderItems), orderEntity.getDeliveryFee(), orderEntity.getCouponId(), member.getId());
    }

    private List<OrderItem> getOrderItems(final Long orderId) {
        return orderItemDao.findByOrderId(orderId).stream()
                .map(OrderItemEntity::toDomain)
                .collect(Collectors.toList());
    }

    private Member getMember(final Long memberId) {
        final MemberEntity memberEntity = memberDao.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        return memberEntity.toDomain();
    }

    public Optional<Order> findById(final Long id) {
        return orderDao.findById(id)
                .map(this::mapToOrder);
    }
}
