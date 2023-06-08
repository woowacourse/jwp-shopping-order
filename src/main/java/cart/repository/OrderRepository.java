package cart.repository;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.dto.OrderItemProductDto;
import cart.dao.dto.OrderWithMemberDto;
import cart.dao.entity.OrderItemEntity;
import cart.domain.Member;
import cart.domain.Order;
import cart.exception.notfoundexception.OrderNotFoundException;
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
        OrderWithMemberDto orderWithMemberDto = orderDao.findById(id)
            .orElseThrow(() -> new OrderNotFoundException(id));
        List<OrderItemProductDto> orderItemProducts = orderItemDao.findAllByOrderId(id);
        return OrderMapper.toOrder(orderWithMemberDto, orderItemProducts);
    }

    public List<Order> findAllByMember(Member member) {
        List<OrderWithMemberDto> ordersWithMember = orderDao.findAllByMemberId(member.getId());
        List<OrderItemProductDto> orderItems = findAllByOrders(ordersWithMember);
        Map<OrderWithMemberDto, List<OrderItemProductDto>> orderItemsByOrder = orderItems.stream()
            .collect(
                Collectors.groupingBy(orderProduct -> findMatchingOrderEntityById(ordersWithMember,
                    orderProduct.getOrderId()), Collectors.toList()));

        return OrderMapper.toOrdersSortedById(orderItemsByOrder);
    }

    private List<OrderItemProductDto> findAllByOrders(List<OrderWithMemberDto> ordersWithMember) {
        List<Long> orderIds = ordersWithMember.stream()
            .map(OrderWithMemberDto::getId)
            .collect(Collectors.toList());
        if (orderIds.isEmpty()) {
            return Collections.emptyList();
        }
        return orderItemDao.findAllByOrderIds(orderIds);
    }

    private OrderWithMemberDto findMatchingOrderEntityById(
        List<OrderWithMemberDto> ordersWithMember, long id) {
        return ordersWithMember.stream()
            .filter(order -> order.getId().equals(id))
            .findAny()
            .orElseThrow(() -> new OrderNotFoundException(id));
    }

}
