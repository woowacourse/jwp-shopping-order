package cart.repository;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.OrderResultMap;
import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderItemEntity;
import cart.dao.entity.ProductEntity;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Product;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
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
        OrderEntity orderEntity = null;
        if (ObjectUtils.isEmpty(order.getCoupon())) {
            orderEntity = new OrderEntity(order.getPrice(), null, order.getMember().getId());
        } else {
            orderEntity = new OrderEntity(order.getPrice(), order.getCoupon().getId(), order.getMember().getId());
        }

        final Long orderId = orderDao.save(orderEntity);

        final List<OrderItemEntity> orderItemEntities = order.getOrderItems().stream()
                .map(orderItem -> new OrderItemEntity(orderItem.getProduct().getId(), orderId, orderItem.getQuantity()))
                .collect(toList());

        orderItemDao.saveAll(orderItemEntities);
        return orderId;
    }

    @Override
    public List<Order> findOrderByMemberId(final Long memberId) {
        final List<OrderResultMap> resultMaps = orderDao.findByMemberId(memberId);
        final Map<Long, List<OrderResultMap>> orders = resultMaps.stream()
                .collect(groupingBy(OrderResultMap::getOrderId));

        final List<Order> orderCatalogs = new ArrayList<>();
        for (Long orderId : orders.keySet()) {
            final List<OrderItem> orderItems = new ArrayList<>();
            final List<OrderResultMap> orderResultMaps = orders.get(orderId);
            for (OrderResultMap orderResultMap : orderResultMaps) {
                final ProductEntity productEntity = orderResultMap.getProductEntity();
                final Product product = new Product(productEntity.getId(), productEntity.getName(), productEntity.getPrice(), productEntity.getImageUrl());
                final OrderItem orderItem = new OrderItem(orderResultMap.getOrderItemId(), product, orderResultMap.getQuantity());
                orderItems.add(orderItem);
            }
            final Order order = new Order(orderId, orderItems, null, null, orderResultMaps.get(0).getPrice(), orderResultMaps.get(0).getDate());
            orderCatalogs.add(order);
        }

        return orderCatalogs;
    }
}
