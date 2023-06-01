package cart.application;

import cart.domain.Cart;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.dto.OrderRequest;
import cart.repository.OrderRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private final CartItemService cartItemService;
    private final OrderRepository orderRepository;

    public OrderService(CartItemService cartItemService, OrderRepository orderRepository) {
        this.cartItemService = cartItemService;
        this.orderRepository = orderRepository;
    }

    public long createOrder(OrderRequest request, Member member) {
        Cart cart = cartItemService.findAllByIds(request.getCartItemIds());
        cart.validateOwner(member);
        Order order = mapCartToOrderProducts(cart);
        cartItemService.removeOrderedItems(cart);
        return orderRepository.save(order, member);
    }

    private Order mapCartToOrderProducts(Cart cart) {
        List<OrderItem> orderItems = cart.getItems()
            .stream()
            .map(this::cartItemToOrderItem)
            .collect(Collectors.toList());
        return new Order(orderItems);
    }

    private OrderItem cartItemToOrderItem(CartItem cartItem) {
        return new OrderItem(cartItem.getProduct(), cartItem.getQuantity());
    }

    @Transactional(readOnly = true)
    public List<Order> findAllByMember(Member member) {
        return orderRepository.findAllByMemberId(member.getId());
    }

    @Transactional(readOnly = true)
    public Order findById(long id, Member member) {
        return orderRepository.findById(id);
    }
}
