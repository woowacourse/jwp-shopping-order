package cart.application.service;

import cart.application.domain.CartItem;
import cart.application.domain.Member;
import cart.application.domain.Order;
import cart.ui.dto.request.OrderRequest;
import cart.ui.dto.response.OrderResponse;
import cart.application.repository.CartItemRepository;
import cart.application.repository.OrderRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;

    public OrderService(final OrderRepository orderRepository, final CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public Long createOrder(final Member member, final OrderRequest orderRequest) {
        final List<CartItem> cartItems = cartItemRepository.findAllByIds(orderRequest.getCartItemIds());

        final int price = cartItems.stream()
                .mapToInt(CartItem::getTotalPrice)
                .sum();

        final Order order = orderRepository.save(new Order(price, member, cartItems));
        return order.getId();
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(final Long id, final Member member) {
        final Order order = orderRepository.findById(id, member);
        return OrderResponse.of(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAll(final Member member) {
        final List<Order> orders = orderRepository.findMemberOrders(member);
        return orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toUnmodifiableList());
    }
}
