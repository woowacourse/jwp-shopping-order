package cart.service;

import static java.util.stream.Collectors.toUnmodifiableList;

import cart.service.request.OrderRequestDto;
import cart.service.response.OrderResponseDto;
import cart.dao.CartItemDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.repository.OrderRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemDao cartItemDao;

    public OrderService(final OrderRepository orderRepository, final CartItemDao cartItemDao) {
        this.orderRepository = orderRepository;
        this.cartItemDao = cartItemDao;
    }

    public OrderResponseDto findOrderById(final Long id) {
        return null;
    }

    public Long order(final Member member, final OrderRequestDto requestDto) {
        final List<CartItem> cartItems = requestDto.getCartItemIds().stream()
                .map(cartItemDao::findById)
                .collect(toUnmodifiableList());
        final Order order = Order.of(member, cartItems);

        final Order savedOrder = orderRepository.save(order);
        for (final CartItem cartItem : cartItems) {
            cartItemDao.delete(cartItem.getMember().getId(), cartItem.getProduct().getId());
        }

        return savedOrder.getId();
    }
}
