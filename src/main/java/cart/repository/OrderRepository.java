package cart.repository;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.ShippingDiscountPolicyDao;
import cart.dao.ShippingFeeDao;
import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderItemEntity;
import cart.dao.entity.ShippingDiscountPolicyEntity;
import cart.dao.entity.ShippingFeeEntity;
import cart.domain.Member;
import cart.domain.ShippingDiscountPolicy;
import cart.domain.ShippingFee;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public Order findOrderById(final Member member, final Long orderId) {
        //TODO: optional 반환 예외처리
        final OrderEntity orderEntity = orderDao.findById(orderId).get();
        final List<OrderItem> orderItemList = findOrderItemsByOrderId(orderId);

        return new Order(orderEntity.getId(),
                member,
                orderEntity.getShippingFee(),
                orderEntity.getTotalPrice(),
                orderItemList,
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
                        orderEntity.getShippingFee(),
                        orderEntity.getTotalPrice(),
                        toOrderItemList(maps.get(orderEntity.getId())),
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
        // TODO: optional 에러처리
        ShippingFeeEntity shippingFeeEntity = shippingFeeDao.findFee().get();
        return ShippingFee.from(shippingFeeEntity);
    }

    public ShippingDiscountPolicy findShippingDiscountPolicy() {
        ShippingDiscountPolicyEntity shippingDiscountPolicyEntity = shippingDiscountPolicyDao.findShippingDiscountPolicy().get();
        return ShippingDiscountPolicy.from(shippingDiscountPolicyEntity);
    }
}
