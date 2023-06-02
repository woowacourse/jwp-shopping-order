package cart.repository;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.PointHistoryDao;
import cart.domain.*;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import cart.entity.PointEntity;
import cart.entity.PointHistoryEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final PointHistoryDao pointHistoryDao;

    public OrderRepository(OrderDao orderDao, OrderItemDao orderItemDao, PointHistoryDao pointHistoryDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.pointHistoryDao = pointHistoryDao;
    }

    public Long save(Long memberId, Order order) {
        OrderEntity orderEntity = new OrderEntity(memberId, order.getOrderStatus().getOrderStatusId());
        Long orderId = orderDao.save(orderEntity);

        List<OrderItemEntity> orderItemEntities = getOrderItemEntities(order);

        orderItemDao.saveAll(orderId, orderItemEntities);

        List<PointEntity> pointEntities = getPointEntities(order);
        pointHistoryDao.saveAll(orderId, pointEntities);
        return orderId;
    }

    private List<OrderItemEntity> getOrderItemEntities(Order order) {
        return order.getOrderItems().stream()
                .map(orderItem -> new OrderItemEntity(orderItem.getProduct(), orderItem.getQuantity(), orderItem.getTotalPrice()))
                .collect(Collectors.toList());
    }

    private List<PointEntity> getPointEntities(Order order) {
        return order.getUsedPoints()
                .stream()
                .map(point -> new PointEntity(point.getId(), point.getValue(), point.getComment(),
                                point.getCreateAt(), point.getExpiredAt()))
                .collect(Collectors.toList());
    }

    public void deleteById(Long orderId) {
        orderDao.deleteById(orderId);
    }

    public List<Order> findAllByMemberId(Long memberId) {
        List<OrderEntity> orderEntities = orderDao.findByMemberId(memberId);
        List<Long> orderIds = getOrderIds(orderEntities);

        List<OrderItemEntity> orderItemEntities = orderItemDao.findAllByOrderIds(orderIds);

        Map<Long, List<OrderItemEntity>> itemsByOrder = getItemsByOrder(orderEntities, orderItemEntities);

        return getOrders(orderEntities, itemsByOrder);
    }

    private List<Long> getOrderIds(List<OrderEntity> orderEntities) {
        return orderEntities.stream()
                .map(OrderEntity::getId)
                .collect(Collectors.toList());
    }

    private Map<Long, List<OrderItemEntity>> getItemsByOrder(List<OrderEntity> orderEntities, List<OrderItemEntity> orderItems) {
        Map<Long, List<OrderItemEntity>> itemsByOrder = new HashMap<>();
        for (OrderEntity orderEntity : orderEntities) {
            itemsByOrder.put(orderEntity.getId(), new ArrayList<>());
        }

        for (OrderItemEntity orderItemEntity : orderItems) {
            List<OrderItemEntity> orderItemEntities = itemsByOrder.get(orderItemEntity.getOrderId());
            orderItemEntities.add(orderItemEntity);
        }
        return itemsByOrder;
    }

    private List<Order> getOrders(List<OrderEntity> orderEntities, Map<Long, List<OrderItemEntity>> itemsByOrder) {
        List<Order> orders = new ArrayList<>();
        for (OrderEntity orderEntity : orderEntities) {
            Long id = orderEntity.getId();
            OrderStatus orderStatus = OrderStatus.findOrderStatusById(orderEntity.getOrderStatusId());
            List<OrderItem> orderItems = getOrderItems(itemsByOrder, id);
            Points points = getPoints(id);
            LocalDate createAt = orderEntity.getCreateAt();
            orders.add(new Order(id, orderStatus, points, orderItems, createAt));
        }
        return orders;
    }

    private List<OrderItem> getOrderItems(Map<Long, List<OrderItemEntity>> itemsByOrder, Long id) {
        return itemsByOrder.get(id).stream()
                .map(orderItemEntity -> new OrderItem(orderItemEntity.getProduct(),
                        orderItemEntity.getQuantity(), orderItemEntity.getTotalPrice()))
                .collect(Collectors.toList());
    }

    private Points getPoints(Long id) {
        List<PointHistoryEntity> pointHistoryEntities = pointHistoryDao.findByOrderId(id);
        return new Points(pointHistoryEntities.stream()
                .map(pointHistoryEntity -> Point.from(pointHistoryEntity.getUsedPoint()))
                .collect(Collectors.toList()));
    }
}
