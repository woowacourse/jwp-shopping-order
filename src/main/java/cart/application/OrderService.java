package cart.application;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderResponse;
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

    public OrderResponse selectOrder(final Long id, final Member member) {
        final Order order = orderRepository.findById(id, member);
        return OrderResponse.of(order);
    }
}
