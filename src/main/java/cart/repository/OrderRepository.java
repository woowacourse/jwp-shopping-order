package cart.repository;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderItemEntity;
import cart.domain.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public OrderRepository(final OrderDao orderDao, final OrderItemDao orderItemDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
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
                .map(orderEntity -> new Order(
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
}
