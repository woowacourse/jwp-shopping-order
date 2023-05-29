package cart.order.application;

import cart.cartitem.domain.CartItem;
import cart.cartitem.domain.CartItemRepository;
import cart.order.application.dto.PlaceOrderCommand;
import cart.order.domain.Order;
import cart.order.domain.OrderRepository;
import cart.order.domain.service.OrderPlaceService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderPlaceService orderPlaceService;
    private final CartItemRepository cartItemRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderPlaceService orderPlaceService,
            CartItemRepository cartItemRepository
    ) {
        this.orderPlaceService = orderPlaceService;
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public Long place(PlaceOrderCommand command) {
        List<CartItem> cartItems = command.getCartItemIds().stream()
                .map(cartItemRepository::findById)
                .collect(Collectors.toList());
        // TODO  생성 이후 장바구니 비워주기
        Order order = orderPlaceService.placeOrder(command.getMemberId(), cartItems);
        return orderRepository.save(order);
    }
}
