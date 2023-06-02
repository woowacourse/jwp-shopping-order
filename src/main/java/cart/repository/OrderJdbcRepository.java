package cart.repository;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.dto.OrderResultMap;
import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderItemEntity;
import cart.dao.entity.ProductEntity;
import cart.domain.Product;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.order.OrderRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Repository
public class OrderJdbcRepository implements OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public OrderJdbcRepository(OrderDao orderDao, OrderItemDao orderItemDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
    }

    @Override
    public Long save(final Order order) {
        OrderEntity orderEntity = mapToOrderEntity(order);

        final Long orderId = orderDao.save(orderEntity);

        final List<OrderItemEntity> orderItemEntities = order.getOrderItems().stream()
                .map(orderItem -> new OrderItemEntity(orderItem.getProduct().getId(), orderId, orderItem.getQuantity()))
                .collect(toList());

        orderItemDao.saveAll(orderItemEntities);
        return orderId;
    }

    private static OrderEntity mapToOrderEntity(final Order order) {
        if (ObjectUtils.isEmpty(order.getCoupon())) {
            return new OrderEntity(order.getPrice(), null, order.getMember().getId());
        }

        return new OrderEntity(order.getPrice(), order.getCoupon().getCouponTypeId(), order.getMember().getId());
    }

    @Override
    public List<Order> findOrderByMemberId(final Long memberId) {
        final List<OrderResultMap> orderResultMaps = orderDao.findByMemberId(memberId);

        final Map<Long, List<OrderResultMap>> orderResultMapGroups = orderResultMaps.stream()
                .collect(groupingBy(OrderResultMap::getOrderId));

        return mapToOrders(orderResultMapGroups);
    }

    private List<Order> mapToOrders(final Map<Long, List<OrderResultMap>> orders) {
        return orders.keySet().stream()
                .map(it -> mapToOrder(orders, it))
                .collect(toList());
    }

    private Order mapToOrder(final Map<Long, List<OrderResultMap>> orders, final Long orderId) {
        final List<OrderResultMap> orderResultMaps = orders.get(orderId);
        final List<OrderItem> orderItems = orderResultMaps.stream()
                .map(this::createOrderItem)
                .collect(toList());

        return new Order(orderId, orderItems, null, null, orderResultMaps.get(0).getPrice(), orderResultMaps.get(0).getDate());
    }

    private OrderItem createOrderItem(final OrderResultMap orderResultMap) {
        final ProductEntity productEntity = orderResultMap.getProductEntity();
        final Product product = new Product(productEntity.getId(), productEntity.getName(), productEntity.getPrice(), productEntity.getImageUrl());
        return new OrderItem(orderResultMap.getOrderItemId(), product, orderResultMap.getQuantity());
    }
}
