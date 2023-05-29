package cart.order.domain.service;

import cart.cartitem.domain.CartItem;
import cart.order.domain.Order;
import cart.order.domain.OrderItem;
import cart.order.domain.OrderValidator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OrderPlaceService {

    private final OrderValidator orderValidator;

    public OrderPlaceService(OrderValidator orderValidator) {
        this.orderValidator = orderValidator;
    }

    public Order placeOrder(Long memberId, List<CartItem> cartItems) {
        orderValidator.validate(memberId, cartItems);
        List<OrderItem> orderItems = cartItems.stream()
                .map(OrderItem::from)
                .collect(Collectors.toList());
        return new Order(memberId, orderItems);
    }
}
