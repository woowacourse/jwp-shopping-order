package cart.application;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderCalculator;
import cart.dto.OrderRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final CartItemService cartItemService;
    private final OrderCalculator orderCalculator;

    public OrderService(CartItemService cartItemService, OrderCalculator orderCalculator) {
        this.cartItemService = cartItemService;
        this.orderCalculator = orderCalculator;
    }

    @Transactional
    public Long add(Member member, OrderRequest orderRequest) {
        final List<CartItem> cartItems = cartItemService.findByIds(orderRequest.getCartItems());
        final Order order = Order.of(member, cartItems);
        orderCalculator.checkPaymentAmount(order, orderRequest.getPaymentAmount());

        return null;
    }
}
