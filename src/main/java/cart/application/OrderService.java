package cart.application;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Money;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.exception.OrderException;
import cart.repository.OrderRepository;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemService cartItemService;

    public OrderService(final OrderRepository orderRepository, final CartItemService cartItemService) {
        this.orderRepository = orderRepository;
        this.cartItemService = cartItemService;
    }

    public Long add(final Member member, final OrderRequest orderRequest) {
        final List<Long> cartItemIds = orderRequest.getCartItemIds();
        final List<CartItem> cartItems = cartItemIds.stream()
                .map(id -> cartItemService.checkMember(member, id))
                .collect(Collectors.toList());

        final Order order = new Order(member, new Money(orderRequest.getDeliveryFee()), OrderItem.convert(cartItems));
        order.checkTotalPrice(orderRequest.getTotalPrice());

        cartItemService.remove(member, cartItemIds);
        return orderRepository.add(order);
    }

    public List<OrderResponse> findOrdersByMember(final Member member) {
        final List<Order> orders = orderRepository.findByMember(member);
        return OrderResponse.from(orders);
    }

    public OrderDetailResponse findOrderDetailById(final Member member, final Long orderId) {
        final Order order = checkMember(member, orderId);
        return OrderDetailResponse.from(order);
    }

    public void remove(final Member member, final Long orderId) {
        checkMember(member, orderId);
        orderRepository.remove(orderId);
    }

    private Order checkMember(final Member member, final Long orderId) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException.IllegalId(orderId));
        if (!Objects.equals(member.getId(), order.getMember().getId())) {
            throw new OrderException.IllegalMember(order, member);
        }
        return order;
    }

    public void cancel(final Member member, final Long orderId) {
        final Order order = checkMember(member, orderId);
        orderRepository.update(order.cancel());
    }
}
