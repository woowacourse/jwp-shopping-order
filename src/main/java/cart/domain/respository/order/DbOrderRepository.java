package cart.domain.respository.order;

import cart.dao.OrderDao;
import cart.domain.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.order.OrderItems;
import cart.domain.order.OrderPrice;
import cart.dto.OrderDto;
import cart.exception.OrderException.OrderNotExistException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class DbOrderRepository implements OrderRepository {

    private final OrderDao orderDao;

    public DbOrderRepository(final OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public Order insert(final Order order, final OrderPrice orderPrice) {
        return orderDao.insert(order, orderPrice);
    }

    @Override
    public Order findByOrderId(final Long orderId) {
        final List<OrderDto> orderDtos = orderDao.findByOrderId(orderId);
        return makeOrder(orderDtos);
    }

    private Order makeOrder(final List<OrderDto> orderDtos) {
        final OrderDto orderDto = orderDtos.stream()
            .findAny()
            .orElseThrow(() -> new OrderNotExistException("주문이 존재하지 않습니다."));

        final Long orderId = orderDto.getOrderId();
        final LocalDateTime orderTime = orderDto.getOrderTime();
        final Member member = orderDto.getMember();
        final List<OrderItem> orderItems = orderDtos.stream()
            .map(OrderDto::getOrderItem)
            .collect(Collectors.toList());

        return Order.persisted(orderId, member, new OrderItems(orderItems), orderTime);
    }

    @Override
    public List<Order> findAllByMemberId(final Long memberId) {
        final Map<Long, List<OrderDto>> memberOrderDtos = orderDao.findAllByMemberId(memberId).stream()
            .collect(Collectors.groupingBy(OrderDto::getOrderId));
        if (memberOrderDtos.isEmpty()) {
            return Collections.emptyList();
        }
        return memberOrderDtos.values().stream()
            .map(this::makeOrder)
            .collect(Collectors.toList());
    }
}
