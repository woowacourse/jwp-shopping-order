package cart.repository;

import cart.dao.*;
import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderItemEntity;
import cart.dao.entity.ShippingDiscountPolicyEntity;
import cart.dao.entity.ShippingFeeEntity;
import cart.domain.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.point.Point;
import cart.domain.shipping.ShippingDiscountPolicy;
import cart.domain.shipping.ShippingFee;
import cart.exception.order.OrderException;
import cart.exception.policy.PolicyException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final ShippingFeeDao shippingFeeDao;
    private final ShippingDiscountPolicyDao shippingDiscountPolicyDao;

    public OrderRepository(OrderDao orderDao, OrderItemDao orderItemDao, ShippingFeeDao shippingFeeDao, ShippingDiscountPolicyDao shippingDiscountPolicyDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.shippingFeeDao = shippingFeeDao;
        this.shippingDiscountPolicyDao = shippingDiscountPolicyDao;
    }

    public Long saveOrder(final Member member, final Order order) {
        final OrderEntity orderEntity = new OrderEntity(
                member.getId(),
                order.getShippingFee().getFee(),
                order.getTotalPrice(),
                order.getUsedPoint()
        );
        final Long orderId = orderDao.createOrder(orderEntity);

        final List<OrderItem> orderItemList = order.getOrderItems();
        final List<OrderItemEntity> orderItemEntityList = orderItemList.stream()
                .map(orderItem -> OrderItemEntity.toEntity(orderItem, orderId))
                .collect(toList());
        saveOrderItems(orderItemEntityList);


        return orderId;
    }

    private void saveOrderItems(List<OrderItemEntity> orderItemEntityList) {
        for (OrderItemEntity orderItemEntity : orderItemEntityList) {
            orderItemDao.createOrderItem(orderItemEntity);
        }
    }

    public Order findOrderById(final Member member, final Long orderId) {
        final OrderEntity orderEntity = orderDao.findById(orderId).orElseThrow(OrderException.NotExistOrder::new);
        final List<OrderItem> orderItemList = findOrderItemsByOrderId(orderId);

        return new Order(orderEntity.getId(),
                member,
                new ShippingFee(orderEntity.getShippingFee()),
                orderEntity.getTotalProductsPrice(),
                orderItemList,
                new Point(orderEntity.getUsedPoint()),
                orderEntity.getCreatedAt());
    }

    private List<OrderItem> findOrderItemsByOrderId(final Long orderId) {
        final List<OrderItemEntity> orderItemEntityList = orderItemDao.findAllByOrderId(orderId);
        return orderItemEntityList.stream()
                .map(orderItemEntity -> new OrderItem(orderItemEntity.toProduct(), orderItemEntity.getQuantity()))
                .collect(toList());
    }

    public List<Order> findAllOrdersByMember(final Member member) {
        final List<OrderEntity> orderEntityList = orderDao.findByMemberId(member.getId());
        final List<Long> orderIds = orderEntityList.stream()
                .map(OrderEntity::getId)
                .collect(toList());

        final Map<Long, List<OrderItemEntity>> maps = orderItemDao.findAllByOrderIds(orderIds).stream()
                .collect(groupingBy(OrderItemEntity::getOrderId));

        return toOrderList(member, orderEntityList, maps);
    }

    private List<Order> toOrderList(final Member member,
                                    final List<OrderEntity> orderEntityList,
                                    final Map<Long, List<OrderItemEntity>> maps) {
        return orderEntityList.stream()
                .map(orderEntity -> new Order(orderEntity.getId(),
                        member,
                        new ShippingFee(orderEntity.getShippingFee()),
                        orderEntity.getTotalProductsPrice(),
                        toOrderItemList(maps.get(orderEntity.getId())),
                        new Point(orderEntity.getUsedPoint()),
                        orderEntity.getCreatedAt()))
                .collect(toList());
    }

    private List<OrderItem> toOrderItemList(final List<OrderItemEntity> orderItemEntityList) {
        return orderItemEntityList.stream()
                .map(orderItemEntity -> new OrderItem(
                        orderItemEntity.toProduct(),
                        orderItemEntity.getQuantity()))
                .collect(toList());
    }

    public ShippingFee findShippingFee() {
        final ShippingFeeEntity shippingFeeEntity = shippingFeeDao.findFee()
                .orElseThrow(PolicyException.NotExistShippingFee::new);
        return ShippingFee.from(shippingFeeEntity);
    }

    public ShippingDiscountPolicy findShippingDiscountPolicy() {
        final ShippingDiscountPolicyEntity shippingDiscountPolicyEntity = shippingDiscountPolicyDao.findShippingDiscountPolicy()
                .orElseThrow(PolicyException.NoShippingDiscountThreshold::new);
        return ShippingDiscountPolicy.from(shippingDiscountPolicyEntity);
    }

}
