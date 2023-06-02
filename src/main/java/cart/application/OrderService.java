package cart.application;

import cart.domain.Member;
import cart.domain.Order;
import cart.dto.OrderResponse;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<OrderResponse> findByMember(Member member) {
        List<Order> orders = orderRepository.findByMemberId(member.getId());
        return orders.stream().map(OrderResponse::of).collect(toList());
    }
}
