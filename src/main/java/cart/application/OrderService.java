package cart.application;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Point;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderItemResponse;
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
        List<OrderItem> orderItems = findOrderItems(cartItems);

        Point usedPoint = new Point(orderRequest.getPoint());
        member.usePoint(usedPoint);

        int totalPrice = calculateTotalPoint(orderItems);
        Point savedPoint = Point.calcualtePoint(totalPrice);
        member.savePoint(savedPoint);

        Order order = new Order(member, orderItems, usedPoint, savedPoint);
        Long orderId = orderRepository.save(order);

        memberRepository.updatePoint(member);
        cartItemRepository.deleteByIds(cartItemIds);

        return orderId;
    }

    public List<OrderDetailResponse> findByMember(Member member) {
        List<Order> orders = orderRepository.findByMemberId(member.getId());
        orders.forEach(order -> order.checkOwner(member));

        return orders.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public OrderDetailResponse findById(Member member, Long id) {
        Order order = orderRepository.findById(id);
        order.checkOwner(member);

        return convertToResponse(order);
    }

    private int calculateTotalPoint(List<OrderItem> orderItems) {
        return orderItems.stream()
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

    private OrderDetailResponse convertToResponse(Order order) {
        return new OrderDetailResponse(
                order.getId(),
                order.getOrderedAt(),
                order.getUsedPoint().getPoint(),
                order.getSavedPoint().getPoint(),
                OrderItemResponse.of(order.getOrderItems())
        );
    }
}
