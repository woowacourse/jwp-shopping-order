package cart.application;

import cart.Repository.OrderRepository;
import cart.domain.CartItem;
import cart.domain.Member.Member;
import cart.domain.Order.Order;
import cart.domain.Order.OrderItem;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemService cartItemService;


    public OrderService(OrderRepository orderRepository, CartItemService cartItemService) {
        this.orderRepository = orderRepository;
        this.cartItemService = cartItemService;
    }

    public Long add(Member member, OrderRequest orderRequest) {
        List<CartItem> cartItems = cartItemService.findByCartItemIds(orderRequest.getCartItemIds());
        cartItems.forEach(it -> it.checkOwner(member));

        List<OrderItem> orderItems = cartItems.stream().map(it ->
                new OrderItem(
                        it.getProduct(),
                        it.getQuantity()
                )
        ).collect(Collectors.toUnmodifiableList());

        Order order = new Order(orderItems);
        Long orderId = orderRepository.save(member, order);

        List<Long> cartIds = cartItems.stream().map(CartItem::getId).collect(Collectors.toUnmodifiableList());
        cartItemService.removeByIds(cartIds);

        return orderId;
    }

    public List<OrderResponse> findByMember(Member member) {
        List<Order> orders = orderRepository.findByMemberId(member.getId());

        return orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toUnmodifiableList());
    }


    public OrderResponse findById(Long id, Member member) {
        Order order = orderRepository.findById(id);
        return OrderResponse.of(order);
    }
}
