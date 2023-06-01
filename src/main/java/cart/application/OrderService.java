package cart.application;

import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.repository.CartItemRepository;
import cart.repository.OrderRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final CartItemRepository cartItemRepository;

    public OrderService(OrderRepository orderRepository, CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
    }

    // TODO: 예외 정리하기
    public Order createDraftOrder(Member member, List<Long> cartItemIds) {
        List<OrderItem> orderItems = cartItemIds.stream()
                .map(cartItemId -> cartItemRepository.findById(cartItemId).orElseThrow())
                .map(OrderItem::from)
                .collect(Collectors.toList());
        return new Order(member, orderItems);
    }
}
