package cart.event;

import cart.domain.order.Order;
import cart.service.CartItemService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventHandler {

    private final CartItemService cartItemService;

    public OrderEventHandler(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @EventListener
    public void deleteCartItems(Order order) {
        cartItemService.deleteByMemberId(order.getMemberId());
    }
}
