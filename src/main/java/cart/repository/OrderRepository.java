package cart.repository;

import cart.dao.MemberDao;
import cart.dao.OrderItemDao;
import cart.dao.OrdersDao;
import cart.domain.Member;
import cart.domain.OrderItem;
import cart.domain.Orders;
import cart.entity.OrderItemEntity;
import cart.entity.OrdersEntity;
import cart.exception.MemberNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {
    
    private final OrdersDao ordersDao;
    private final OrderItemDao orderItemDao;
    private final MemberDao memberDao;

    public OrderRepository(final OrdersDao ordersDao, final OrderItemDao orderItemDao, final MemberDao memberDao) {
        this.ordersDao = ordersDao;
        this.orderItemDao = orderItemDao;
        this.memberDao = memberDao;
    }

    public Orders save(final Orders orders) {
        final OrdersEntity ordersEntity = new OrdersEntity(
                orders.getDeliveryFee(),
                null,
                orders.getMember().getId());

        final OrdersEntity savedOrders = ordersDao.insert(ordersEntity);
        final Long savedOrderId = savedOrders.getId();

        List<OrderItem> savedOrderItems = new ArrayList<>();
        for (OrderItem orderItem : orders.getOrderItems()) {
            final OrderItemEntity orderItemEntity = new OrderItemEntity(
                    orderItem.getName(),
                    orderItem.getPrice(),
                    orderItem.getImageUrl(),
                    orderItem.getQuantity(),
                    savedOrderId);
            final OrderItemEntity savedOrderItem = orderItemDao.insert(orderItemEntity);
            savedOrderItems.add(new OrderItem(
                    savedOrderItem.getId(),
                    savedOrderItem.getName(),
                    savedOrderItem.getPrice(),
                    savedOrderItem.getImageUrl(),
                    savedOrderItem.getQuantity()
            ));
        }

        return new Orders(
                savedOrderId,
                3000L,
                null,
                orders.getMember(),
                savedOrderItems
        );
    }

    public Orders findByOrderIdAndMemberId(final Long orderId, final Long memberId) {
        OrdersEntity ordersEntity = ordersDao.findByOrderIdAndMemberId(orderId, memberId);

        List<OrderItem> orderItems = makeOrderItems(orderItemDao.findAllByOrderId(orderId));
        Member member = getMember(memberId);
        // 쿠폰은 아직 null
        // ordersEntity.getCouponId();

        return new Orders(
                orderId,
                ordersEntity.getDeliveryFee(),
                null,
                member,
                orderItems
        );
    }

    private List<OrderItem> makeOrderItems(final List<OrderItemEntity> orderItemEntities) {
        return orderItemEntities.stream()
                .map(it -> new OrderItem(it.getId(), it.getName(), it.getPrice(), it.getImageUrl(), it.getQuantity()))
                .collect(Collectors.toList());
    }

    private Member getMember(final Long memberId) {
        return memberDao.findById(memberId)
                .orElseThrow(MemberNotFoundException::new)
                .toDomain();
    }

    public List<Orders> findAllByMemberId(final Long memberId) {
        List<Orders> orders = new ArrayList<>();
        List<OrdersEntity> findOrderEntities = ordersDao.findByMemberId(memberId);
        Member member = getMember(memberId);
        for (OrdersEntity findOrderEntity : findOrderEntities) {
            Long orderEntityId = findOrderEntity.getId();
            orders.add(new Orders(
                    orderEntityId,
                    findOrderEntity.getDeliveryFee(),
                    null,
                    member,
                    makeOrderItems(orderItemDao.findAllByOrderId(orderEntityId))
            ));
        }
        return orders;
    }
}
