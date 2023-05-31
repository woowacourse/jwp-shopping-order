package cart.application;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Money;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.exception.CartItemException;
import cart.repository.OrderRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    // TODO CartItem 도 Repository 계층 도입 후 관련 메서드 추상화
    private final CartItemDao cartItemDao;
    private final OrderRepository orderRepository;

    public OrderService(final CartItemDao cartItemDao,
                        final OrderDao orderDao,
                        final OrderItemDao orderItemDao) {
        this.cartItemDao = cartItemDao;
        this.orderRepository = new OrderRepository(orderDao, orderItemDao);
    }

    public Long add(final Member member, final OrderRequest orderRequest) {
        final List<Long> cartItemIds = orderRequest.getCartItemIds();
        final List<CartItem> cartItems = findCartItems(member, cartItemIds);

        cartItemDao.deleteByIds(cartItemIds);
        final Order order = new Order(member, OrderItem.convert(cartItems), new Money(orderRequest.getDeliveryFee()));
        order.checkTotalPrice(orderRequest.getTotalPrice());
        return orderRepository.add(order);
    }

    private List<CartItem> findCartItems(final Member member, final List<Long> cartItemIds) {
        return cartItemIds.stream()
                .map(id -> findCartItem(member, id))
                .collect(Collectors.toList());
    }

    private CartItem findCartItem(final Member member, final Long cartItemId) {
        final CartItem cartItem = cartItemDao.findById(cartItemId)
                .orElseThrow(() -> new CartItemException.IllegalId(cartItemId));
        cartItem.checkOwner(member);
        return cartItem;
    }

    public List<OrderResponse> findOrdersByMember(final Member member) {
        final List<Order> orders = orderRepository.findByMember(member);
        return OrderResponse.from(orders);
    }

    public OrderDetailResponse findOrderDetailById(final Member member, final Long orderId) {
        return OrderDetailResponse.from(orderRepository.findById(member, orderId));
    }

    public void remove(final Member member, final Long orderId) {
        orderRepository.remove(member, orderId);
    }
}
