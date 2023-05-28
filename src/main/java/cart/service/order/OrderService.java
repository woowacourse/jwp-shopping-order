package cart.service.order;

import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.dto.order.OrderResponse;
import cart.dto.order.OrdersResponse;
import cart.repository.order.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public OrdersResponse findOrders(final Member member) {
        List<Order> orders = orderRepository.findAllByMemberId(member.getId());
        return OrdersResponse.from(orders);
    }

    @Transactional(readOnly = true)
    public OrderResponse findOrder(final Member member, final Long orderId) {
        // TODO: 다른 유저가 보는지 validate 추가하기
        Order order = orderRepository.findById(orderId);
        return OrderResponse.from(order);
    }
}
