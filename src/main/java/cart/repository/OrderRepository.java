package cart.repository;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.dto.OrderItemProductDto;
import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderItemEntity;
import cart.domain.Member;
import cart.domain.Order;
import cart.exception.IllegalOrderException;
import cart.repository.mapper.OrderMapper;
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

    public long save(Order order, Member member) {
        long orderId = orderDao.save(OrderMapper.toOrderEntity(order, member));
        List<OrderItemEntity> orderItemEntities = order.getItems()
            .stream()
            .map(item -> OrderMapper.toOrderItemEntity(item, orderId))
            .collect(Collectors.toList());
        orderItemDao.batchInsert(orderItemEntities);
        return orderId;
    }

    public Order findById(long id) {
        OrderEntity orderEntity = orderDao.findById(id)
            .orElseThrow(() -> new IllegalOrderException("존재하지 않는 주문 id 입니다."));
        List<OrderItemProductDto> orderItemProducts = orderItemDao.findAllByOrderId(id);
        return OrderMapper.toOrder(orderEntity, orderItemProducts);
    }

    public List<Order> findAllByMemberId(long memberId) {
        List<OrderEntity> orderEntities = orderDao.findAllByMemberId(memberId);
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
        return orderItemDao.findAllByOrderIds(orderIds);
    }

    private OrderEntity findMatchingOrderEntityById(List<OrderEntity> orderEntities, long id) {
        return orderEntities.stream()
            .filter(orderEntity -> orderEntity.getId().equals(id))
            .findAny()
            .orElseThrow(() -> new IllegalOrderException("잘못된 주문 id 요청입니다."));
    }

}
