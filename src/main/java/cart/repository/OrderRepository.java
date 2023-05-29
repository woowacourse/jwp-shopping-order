package cart.repository;

import cart.dao.MemberCouponDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.Order;
import cart.entity.MemberCouponEntity;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final MemberCouponDao memberCouponDao;

    public OrderRepository(final OrderDao orderDao, final OrderItemDao orderItemDao, final MemberCouponDao memberCouponDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.memberCouponDao = memberCouponDao;
    }

    public Order save(final Order order) {
        final OrderEntity orderEntity = OrderEntity.from(order);
        final MemberCouponEntity memberCouponEntity = MemberCouponEntity.from(order.getMemberCoupon());
        memberCouponDao.update(memberCouponEntity);
        if (Objects.isNull(order.getId())) {
            final OrderEntity entity = orderDao.insert(orderEntity);
            final List<OrderItemEntity> orderItemEntities = mapToOrderItemEntities(order, entity.getId());
            orderItemEntities.forEach(orderItemDao::insert);

            return new Order(entity.getId(), order.getOrderItems(), order.getDeliveryFee(), order.getMemberCoupon(), order.getMember());
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
}
