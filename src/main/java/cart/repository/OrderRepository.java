package cart.repository;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.dto.OrderItemProductDto;
import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderItemEntity;
import cart.domain.Member;
import cart.domain.Order;
import cart.exception.OrderNotFoundException;
import cart.repository.mapper.OrderMapper;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public OrderRepository(OrderDao orderDao, OrderItemDao orderItemDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
    }

    public long save(Order order) {
        long orderId = orderDao.save(OrderMapper.toOrderEntity(order));
        List<OrderItemEntity> orderItemEntities = order.getItems()
            .stream()
            .map(item -> OrderMapper.toOrderItemEntity(item, orderId))
            .collect(Collectors.toList());
        orderItemDao.batchInsert(orderItemEntities);
        return orderId;
    }

    public Order findById(long id) {
        OrderEntity orderEntity = orderDao.findById(id)
            .orElseThrow(() -> new OrderNotFoundException(id));
        List<OrderItemProductDto> orderItemProducts = orderItemDao.findAllByOrderId(id);
        return OrderMapper.toOrder(orderEntity, orderItemProducts);
    }

    public List<Order> findAllByMember(Member member) {
        List<OrderEntity> orderEntities = orderDao.findAllByMemberId(member.getId());
        List<OrderItemProductDto> orderItems = findAllByOrders(orderEntities);
        Map<OrderEntity, List<OrderItemProductDto>> orderItemsByOrderEntity = orderItems.stream()
            .collect(Collectors.groupingBy(orderProduct -> findMatchingOrderEntityById(orderEntities,
                orderProduct.getOrderId()), Collectors.toList()));

        return OrderMapper.toOrdersSortedById(orderItemsByOrderEntity);
    }

    private List<OrderItemProductDto> findAllByOrders(List<OrderEntity> orderEntities) {
        List<Long> orderIds = orderEntities.stream()
            .map(OrderEntity::getId)
            .collect(Collectors.toList());
        if (orderIds.isEmpty()) {
            return Collections.emptyList();
        }
        return orderItemDao.findAllByOrderIds(orderIds);
    }

    private OrderEntity findMatchingOrderEntityById(List<OrderEntity> orderEntities, long id) {
        return orderEntities.stream()
            .filter(orderEntity -> orderEntity.getId().equals(id))
            .findAny()
            .orElseThrow(() -> new OrderNotFoundException(id));
    }

}
