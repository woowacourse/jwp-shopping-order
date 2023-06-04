package cart.repository;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import cart.dao.MemberCouponDao;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final OrderCouponDao orderCouponDao;
    private final MemberCouponDao memberCouponDao;

    public OrderRepository(OrderDao orderDao, OrderItemDao orderItemDao, OrderCouponDao orderCouponDao,
                           MemberCouponDao memberCouponDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.orderCouponDao = orderCouponDao;
        this.memberCouponDao = memberCouponDao;
    }

    public Long save(Order order) {
        Long orderId = orderDao.save(OrderEntity.from(order));

        for (OrderItem orderItem : order.getOrderItems()) {
            Long orderItemId = orderItemDao.save(OrderItemEntity.from(orderItem, orderId));
            List<OrderCouponEntity> orderCouponEntities = orderItem.getCoupons().stream()
                    .map(it -> new OrderCouponEntity(orderItemId, it.getCoupon().getId()))
                    .collect(toList());
            orderCouponDao.batchSave(orderCouponEntities);
        }
        return orderId;
    }

    public Order findById(Long orderId) {
        OrderEntity orderEntity = orderDao.findById(orderId)
                .orElseThrow();
        List<OrderItemEntity> orderItemEntities = orderItemDao.findAllByOrderId(orderId);
        List<OrderCouponEntity> orderCouponEntities = orderCouponDao.findAllByOrderId(orderId);
        List<Long> memberCouponIds = orderCouponEntities.stream()
                .map(OrderCouponEntity::getMemberCouponId)
                .collect(toList());
        Map<Long, MemberCoupon> memberCoupons = memberCouponDao.findAllByIds(memberCouponIds).stream()
                .collect(toMap(MemberCoupon::getId, Function.identity()));
        Map<Long, List<MemberCoupon>> orderCoupons = createOrderCouponsByOrderItemId(orderCouponEntities,
                memberCoupons);
        return createSingleOrder(orderItemEntities, orderCoupons, orderEntity);
    }

    public List<Order> findAllByMemberId(Long memberId) {
        List<OrderEntity> orderEntities = orderDao.findAllByMemberId(memberId);
        Map<Long, OrderItemEntity> orderItemEntities = orderItemDao.findAllByMemberId(memberId).stream().
                collect(toMap(OrderItemEntity::getId, Function.identity()));
        List<OrderCouponEntity> orderCouponEntities = orderCouponDao.findAllByMemberId(memberId);
        List<Long> memberCouponIds = orderCouponEntities.stream()
                .map(OrderCouponEntity::getMemberCouponId)
                .collect(toList());
        Map<Long, MemberCoupon> memberCoupons = memberCouponDao.findAllByIds(memberCouponIds).stream()
                .collect(toMap(MemberCoupon::getId, Function.identity()));

        Map<Long, List<MemberCoupon>> orderCoupons = createOrderCouponsByOrderItemId(orderCouponEntities,
                memberCoupons);
        List<Order> orders = new ArrayList<>();
        Map<Long, List<OrderItem>> orderItems = createOrderItems(orderItemEntities, orderCoupons);
        for (OrderEntity orderEntity : orderEntities) {
            Order order = new Order(orderEntity.getId(), orderEntity.getMemberId(), orderItems.get(orderEntity.getId()),
                    orderEntity.getTotalPrice());
            orders.add(order);
        }

        return orders;
    }

    private Order createSingleOrder(List<OrderItemEntity> orderItemEntities,
                                    Map<Long, List<MemberCoupon>> orderCoupons,
                                    OrderEntity orderEntity) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemEntity orderItemEntity : orderItemEntities) {
            OrderItem orderItem = orderItemEntity.toDomain(
                    orderCoupons.getOrDefault(orderItemEntity.getId(), new ArrayList<>()));
            orderItems.add(orderItem);
        }

        return new Order(orderEntity.getId(), orderEntity.getMemberId(), orderItems, orderEntity.getTotalPrice());
    }

    private Map<Long, List<MemberCoupon>> createOrderCouponsByOrderItemId(
            List<OrderCouponEntity> orderCouponEntities,
            Map<Long, MemberCoupon> memberCoupons) {
        Map<Long, List<MemberCoupon>> orderCoupons = new HashMap<>();

        for (OrderCouponEntity orderCouponEntity : orderCouponEntities) {
            orderCoupons.computeIfAbsent(orderCouponEntity.getOrderItemId(), key -> new ArrayList<>()).add(
                    memberCoupons.get(orderCouponEntity.getMemberCouponId()));
        }
        return orderCoupons;
    }

    private Map<Long, List<OrderItem>> createOrderItems(Map<Long, OrderItemEntity> orderItemEntities,
                                                        Map<Long, List<MemberCoupon>> orderCoupons) {
        Map<Long, List<OrderItem>> orderItems = new HashMap<>();
        for (Entry<Long, OrderItemEntity> orderItemEntityEntry : orderItemEntities.entrySet()) {
            OrderItemEntity orderItemEntity = orderItemEntityEntry.getValue();
            List<MemberCoupon> memberCoupons = orderCoupons.getOrDefault(orderItemEntityEntry.getKey(),
                    Collections.emptyList());
            OrderItem orderItem = orderItemEntity.toDomain(memberCoupons);
            orderItems.computeIfAbsent(orderItemEntity.getOrderId(), key ->
                    new ArrayList<>()).add(orderItem);
        }
        return orderItems;
    }
}
