package cart.repository;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.PointHistoryDao;
import cart.domain.Order;
import cart.entity.OrderEntity;
import cart.entity.PointEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    public Long save(Order order) {
        OrderEntity orderEntity = new OrderEntity(order.getMember().getId(), order.getOrderStatus().getOrderStatusId());
        Long orderId = orderDao.save(orderEntity);
        orderItemDao.saveAll(orderId, order.getOrderItems());

        List<PointEntity> pointEntities = order.getUsedPoints()
                .stream()
                .map(point -> new PointEntity(point.getId(), point.getValue(), point.getComment(),
                        point.getCreateAt(), point.getExpiredAt()))
                .collect(Collectors.toList());

        pointHistoryDao.saveAll(orderId, pointEntities);
        return orderId;
    }
}
