package cart.application;

import cart.domain.Cart;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.dto.order.OrderRequest;
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
    private final PointService pointService;

    public OrderService(CartItemService cartItemService, OrderRepository orderRepository,
        PointService pointService) {
        this.cartItemService = cartItemService;
        this.orderRepository = orderRepository;
        this.pointService = pointService;
    }

    public long createOrder(OrderRequest request, Member member) {
        Cart cart = cartItemService.findAllByIds(request.getCartItemIds(), member);
        Order order = saveCartItemsAsOrder(cart, member);
        pointService.savePointOfOrder(request.getUsePoint(), order);
        return order.getId();
    }

    private Order saveCartItemsAsOrder(Cart cart, Member member) {
        Order order = mapCartToOrderProducts(cart, member);
        cartItemService.removeOrderedItems(cart);
        long savedOrderId = orderRepository.save(order);
        return new Order(savedOrderId, order.getItems(), order.getMember());
    }

    private Order mapCartToOrderProducts(Cart cart, Member member) {
        List<OrderItem> orderItems = cart.getItems()
            .stream()
            .map(this::cartItemToOrderItem)
            .collect(Collectors.toList());
        return new Order(orderItems, member);
    }

    private OrderItem cartItemToOrderItem(CartItem cartItem) {
        return new OrderItem(cartItem.getProduct(), cartItem.getQuantity());
    }

    @Transactional(readOnly = true)
    public List<Order> findAllByMember(Member member) {
        return orderRepository.findAllByMember(member);
    }

    @Transactional(readOnly = true)
    public Order findById(long id, Member member) {
        Order order = orderRepository.findById(id);
        order.checkOwner(member);
        return order;
    }
}
