package cart.service.order;

import cart.controller.dto.OrderResponse;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
public class OrderProvider {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderProvider(final OrderRepository orderRepository, final OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    public List<OrderResponse> findOrderByMember(final Member member) {
        final List<Order> orders = orderRepository.findOrderByMemberId(member.getId());
        return orders.stream()
                .map(orderMapper::createOrderResponse)
                .collect(toList());
    }
}
