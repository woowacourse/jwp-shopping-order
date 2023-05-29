package cart.order.domain.service;

import cart.cartitem.domain.CartItem;
import cart.cartitem.domain.CartItemRepository;
import cart.order.domain.Order;
import cart.order.domain.OrderItem;
import cart.order.domain.OrderRepository;
import cart.order.domain.OrderValidator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OrderPlaceService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderValidator orderValidator;

    public OrderPlaceService(OrderRepository orderRepository, CartItemRepository cartItemRepository,
                             OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderValidator = orderValidator;
    }

    public Long placeOrder(Long memberId, List<CartItem> cartItems) {
        orderValidator.validate(memberId, cartItems);
        List<OrderItem> orderItems = cartItems.stream()
                .map(OrderItem::from)
                .collect(Collectors.toList());
        Long id = orderRepository.save(new Order(memberId, orderItems));
        for (CartItem cartItem : cartItems) {
            cartItemRepository.deleteById(cartItem.getId());
        }
        return id;
    }
}
