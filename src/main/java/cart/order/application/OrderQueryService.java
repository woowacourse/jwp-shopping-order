package cart.order.application;

import static cart.order.exception.OrderExceptionType.NO_AUTHORITY_QUERY_ORDER;

import cart.order.domain.Order;
import cart.order.domain.OrderRepository;
import cart.order.exception.OrderException;
import cart.order.presentation.dto.OrderResponse;
import cart.order.presentation.dto.OrderResponses;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class OrderQueryService {

    private final OrderRepository orderRepository;

    public OrderQueryService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderResponses findAllByMemberId(Long memberId) {
        List<Order> orders = orderRepository.findAllByMemberId(memberId);
        Map<Order, Integer> orderPriceMap = orders.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        Order::totalPrice,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
        return OrderResponses.from(orderPriceMap);
    }

    public OrderResponse findByIdAndMemberId(Long id, Long memberId) {
        Order order = orderRepository.findById(id);
        validateOwner(order, memberId);
        return OrderResponse.from(order, order.totalPrice());
    }

    private void validateOwner(Order order, Long memberId) {
        if (!Objects.equals(order.getMemberId(), memberId)) {
            throw new OrderException(NO_AUTHORITY_QUERY_ORDER);
        }
    }
}
