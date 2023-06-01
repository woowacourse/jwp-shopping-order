package cart.step2.order.service;

import cart.step2.order.domain.Order;
import cart.step2.order.domain.repository.OrderRepository;
import cart.step2.order.presentation.dto.OrderCreateRequest;
import cart.step2.order.presentation.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Long create(final Long memberId, final OrderCreateRequest request) {
        Order order = request.toDomain(memberId);
        return orderRepository.save(order);
    }

    public List<OrderResponse> findAllByMemberId(final Long memberId) {
        return orderRepository.findAllByMemberId(memberId).stream()
                .map(order -> new OrderResponse(order.getId(), order.getOrderItems(), order.getDate(), order.getPrice()))
                .collect(Collectors.toList());
    }

}
