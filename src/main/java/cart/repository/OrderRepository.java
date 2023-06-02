package cart.repository;

import static java.util.stream.Collectors.toList;

import cart.dao.OrderCouponDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.MemberCoupon;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.entity.OrderCouponEntity;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final OrderCouponDao orderCouponDao;

    public OrderRepository(OrderDao orderDao, OrderItemDao orderItemDao, OrderCouponDao orderCouponDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.orderCouponDao = orderCouponDao;
    }

    public Long save(Order order) {
        Long orderId = orderDao.save(OrderEntity.from(order));

        for (OrderItem orderItem : order.getOrderItems()) {
            Long orderItemId = orderItemDao.save(OrderItemEntity.from(orderItem, orderId));
            List<OrderCouponEntity> orderCouponEntities = orderItem.getCoupons().stream()
                    .map(coupon -> new OrderCouponEntity(orderItemId, coupon.getCoupon().getId()))
                    .collect(toList());
            orderCouponDao.batchSave(orderCouponEntities);
        }
        return orderId;
    }

    public Order findById(Long id) {
        OrderEntity orderEntity = orderDao.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        return createOrder(id, orderEntity);
    }

    private Order createOrder(Long id, OrderEntity orderEntity) {
        List<OrderItemEntity> orderItemEntities = orderItemDao.findAllByOrderId(id);
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemEntity orderItemEntity : orderItemEntities) {
            List<MemberCoupon> memberCoupons = orderCouponDao.findByOrderItemId(orderItemEntity.getId());
            orderItems.add(orderItemEntity.toDomain(memberCoupons));
        }

        return new Order(orderEntity.getId(), orderEntity.getMemberId(), orderItems, orderEntity.getTotalPrice());
    }

    public List<Order> findAllByMemberId(Long memberId) {
        List<OrderEntity> orderEntities = orderDao.findAllByMemberId(memberId);
        List<Order> orders = new ArrayList<>();
        for (OrderEntity orderEntity : orderEntities) {
            orders.add(createOrder(orderEntity.getId(), orderEntity));
        }

        return orders;
    }
}
