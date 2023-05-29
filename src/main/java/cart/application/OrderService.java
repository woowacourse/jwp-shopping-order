package cart.application;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.repository.CartItemRepository;
import cart.repository.MysqlCartItemRepository;
import cart.repository.MysqlOrderRepository;
import cart.repository.OrderRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;

    public OrderService(final MysqlOrderRepository orderRepository, final MysqlCartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public Long buy(final Member member, final OrderRequest orderRequest) {
        final List<CartItem> cartItems = orderRequest.getCartItemIds().stream()
                .map(cartItemRepository::findById)
                .collect(Collectors.toList());

        final int price = cartItems.stream()
                .mapToInt(CartItem::getTotalPrice)
                .sum();

        final Order order = orderRepository.save(new Order(price, member, cartItems));
        return order.getId();
    }
}
