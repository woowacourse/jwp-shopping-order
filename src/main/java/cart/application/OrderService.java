package cart.application;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.exception.CartItemException;
import cart.exception.OrderException;
import cart.repository.CartItemRepository;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    private final CartItemRepository cartItemRepository;

    public OrderService(final OrderRepository orderRepository, final CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public Order createDraftOrder(final Member member, final List<Long> cartItemIds) {
        if (cartItemIds.isEmpty()) {
            throw new OrderException.EmptyItemInput();
        }
        final List<OrderItem> orderItems = cartItemIds.stream()
                .map(cartItemId -> this.findCartItemOf(cartItemId, member))
                .map(OrderItem::from)
                .collect(Collectors.toList());
        return new Order(member, orderItems);
    }

    private CartItem findCartItemOf(final Long cartItemId, final Member member) {
        final CartItem cartItem = this.cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemException.NotFound(cartItemId));

        if (!member.equals(cartItem.getMember())) {
            throw new CartItemException.IllegalMember(cartItem, member);
        }

        return cartItem;
    }

    public Long createOrderAndSave(final Member member, final List<Long> cartItemIds) {
        final Order draftOrder = this.createDraftOrder(member, cartItemIds);
        final Long orderId = this.orderRepository.create(draftOrder);
        cartItemIds.forEach(this.cartItemRepository::deleteById);
        return orderId;
    }

    @Transactional(readOnly = true)
    public Order retrieveOrderById(final Long orderId) {
        return this.orderRepository.findById(orderId).orElseThrow(() -> new OrderException.NotFound(orderId));
    }

    @Transactional(readOnly = true)
    public List<Order> retrieveMemberOrders(final Member member) {
        final List<Order> memberOrder = this.orderRepository.findByMember(member);
        return memberOrder.stream()
                .sorted(Comparator.comparing(Order::getOrderTime).reversed())
                .collect(Collectors.toList());
    }
}
