package cart.application.service.order;

import cart.application.repository.order.OrderRepository;
import cart.application.service.order.dto.OrderDto;
import cart.domain.order.Order;
import cart.persistence.order.OrderedItemJdbcRepository;
import cart.ui.MemberAuth;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderReadService {

    private final OrderRepository orderRepository;
    private final OrderedItemJdbcRepository orderedItemJdbcRepository;

    public OrderReadService(final OrderRepository orderRepository,
                            final OrderedItemJdbcRepository orderedItemJdbcRepository) {
        this.orderRepository = orderRepository;
        this.orderedItemJdbcRepository = orderedItemJdbcRepository;
    }

    public List<OrderDto> findAllByMember(final MemberAuth memberAuth) {
        List<Order> orders = orderRepository.findAllByMemberId(memberAuth.getId());

        return orders.stream()
                .map(OrderDto::of)
                .collect(Collectors.toUnmodifiableList());
    }

    public OrderDto findByOrderId(final MemberAuth memberAuth, final Long orderId) {
        return OrderDto.of(orderRepository.findById(orderId));
    }

}
