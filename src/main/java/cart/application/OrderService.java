package cart.application;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Point;
import cart.dto.OrderRequest;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.OrderRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;

    public OrderService(OrderRepository orderRepository,
                        CartItemRepository cartItemRepository,
                        MemberRepository memberRepository) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.memberRepository = memberRepository;
    }

    public Long save(Member member, OrderRequest orderRequest) {
        List<Long> cartItemIds = orderRequest.getCartItemIds();
        List<CartItem> cartItems = cartItemRepository.findByIds(member, cartItemIds);
        List<OrderItem> orderedItems = findOrderItems(cartItems);

        Point usedPoint = new Point(orderRequest.getPoint());
        member.usePoint(usedPoint);

        int totalPrice = calculateTotalPoint(orderedItems);
        Point savedPoint = Point.calcualtePoint(totalPrice);
        member.savePoint(savedPoint);

        Order order = Order.of(member, orderedItems, usedPoint, savedPoint);
        Long orderId = orderRepository.save(order);

        memberRepository.updatePoint(member);
        cartItemRepository.deleteByIds(cartItemIds);

        return orderId;
    }

    private int calculateTotalPoint(List<OrderItem> orderedItems) {
        return orderedItems.stream()
                .mapToInt(OrderItem::calculatePrice)
                .sum();
    }

    private List<OrderItem> findOrderItems(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(cartItem -> new OrderItem(
                        cartItem.getProduct(),
                        cartItem.getQuantity()
                ))
                .collect(Collectors.toList());
    }
}
