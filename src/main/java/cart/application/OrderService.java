package cart.application;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderRepository;
import cart.dto.OrderListResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.exception.CartItemException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final CartItemDao cartItemDao;
    private final OrderRepository orderRepository;

    public OrderService(final CartItemDao cartItemDao, final OrderRepository orderRepository) {
        this.cartItemDao = cartItemDao;
        this.orderRepository = orderRepository;
    }

    public Long add(final Member member, final OrderRequest orderRequest) {
        final int usedPoints = orderRequest.getUsedPoints();
        final List<Long> selectedCartItemIds = orderRequest.getCartItemIds();

        final List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        final List<CartItem> orderedCartItems = selectedCartItemIds.stream()
                .map(id -> cartItems.stream()
                        .filter(cartItem -> cartItem.getId().equals(id))
                        .findAny()
                        .orElseThrow(() -> new CartItemException.NotFound(id))).collect(Collectors.toList());

        final Order order = Order.generate(member, usedPoints, orderedCartItems);

        final Long orderId = orderRepository.saveOrder(order);
        orderedCartItems.forEach(item -> cartItemDao.deleteById(item.getId()));

        return orderId;
    }

    public OrderResponse findById(final Member member, final Long id) {
        final Order order = new Order(1L, 1L, List.of(), 0, 0, 0, 0, "");
        return OrderResponse.of(order);
    }

    public OrderListResponse findPageByIndex(final Member member, final Long idx) {
        return OrderListResponse.of(List.of());
    }
}
