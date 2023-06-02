package cart.repository;

import cart.dao.OrderCouponDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.member.MemberCoupon;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final OrderCouponDao orderCouponDao;

    public OrderRepository(final OrderDao orderDao, final OrderItemDao orderItemDao, final OrderCouponDao orderCouponDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.orderCouponDao = orderCouponDao;
    }

    public Long create(List<OrderItem> orderItems, OrderEntity orderEntity) {
        Long orderId = orderDao.create(orderEntity.getMemberId(), orderEntity.getDeliveryFee());

        for (OrderItem orderItem : orderItems) {
            Long orderItemId = orderItemDao.create(orderId, orderItem.getProduct(), orderItem.getQuantity());
            for (MemberCoupon coupon : orderItem.getCoupons()) {
                orderCouponDao.create(orderItemId, coupon.getId());
            }
        }
        return orderId;
    }

    public List<Order> findAllByMemberId(Long memberId) {
        List<OrderEntity> findOrders = orderDao.findAllByMemberId(memberId);

        return findOrders.stream()
                .map(orderEntity -> orderEntity.toOrder(createOrderItems(orderEntity.getId())))
                .collect(Collectors.toList());
    }

    private List<OrderItem> createOrderItems(final Long orderId) {
        List<OrderItemEntity> orderItemEntities = orderItemDao.findAllByOrderId(orderId);

        return orderItemEntities.stream()
                .map(orderItemEntity -> new OrderItem(orderItemEntity, orderCouponDao.findByOrderItemId(orderItemEntity.getId())))
                .collect(Collectors.toList());
    }

    public Optional<Order> findById(Long id) {
        Optional<OrderEntity> findOrder = orderDao.findById(id);

        if (findOrder.isEmpty()) {
            return Optional.empty();
        }

        OrderEntity order = findOrder.get();

        List<OrderItem> orderItems = createOrderItems(order.getId());
        return Optional.of(order.toOrder(orderItems));
    }
}
