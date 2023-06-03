package cart.application;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Money;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.PointDiscountPolicy;
import cart.domain.PointEarnPolicy;
import cart.dto.response.OrderResponse;
import cart.dto.response.SortedOrdersResponse;
import cart.exception.OrderException.NotFound;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.OrderRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final MemberRepository memberRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    public OrderService(
            MemberRepository memberRepository,
            CartItemRepository cartItemRepository,
            OrderRepository orderRepository
    ) {
        this.memberRepository = memberRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Long order(Member member, List<Long> orderCartItemIds, int usedPointsAmount) {
        List<OrderItem> orderCartItems = findOrderCartItems(member, orderCartItemIds);
        Order persistOrder = processOrder(member, orderCartItems, usedPointsAmount);

        processUpdateMemberPoints(member, persistOrder);
        cartItemRepository.deleteByOrderCartItemIds(orderCartItemIds);

        return persistOrder.getId();
    }

    private List<OrderItem> findOrderCartItems(Member member, List<Long> orderCartItemIds) {
        List<CartItem> memberCartItems = cartItemRepository.findByMember(member);

        return calculateOrderCartItems(orderCartItemIds, memberCartItems);
    }

    private Order processOrder(Member member, List<OrderItem> orderCartItems, int usedPointsAmount) {
        Money usedPoints = new Money(usedPointsAmount);
        member.checkUsedPoints(usedPoints);

        Order order = Order.of(
                orderCartItems,
                member.getId(),
                usedPoints,
                PointDiscountPolicy.DEFAULT,
                PointEarnPolicy.DEFAULT
        );

        return orderRepository.save(member, order);
    }

    private void processUpdateMemberPoints(Member member, Order persistOrder) {
        Member usedPointsMember = member.usePoints(persistOrder.getUsedPoints());
        Member earnedPointsMember = usedPointsMember.earnPoints(persistOrder.getEarnedPoints());

        memberRepository.update(earnedPointsMember);
    }

    public OrderResponse findByMemberAndOrderId(Member member, Long orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(NotFound::new);
        order.checkOwner(member);

        return OrderResponse.from(order);
    }

    public SortedOrdersResponse findByMemberAndLastOrderId(Member member, Long lastOrderId, int size) {
        List<Order> sortedOrders = orderRepository.findByMemberAndLastOrderIdAndSize(member, lastOrderId, size);

        return SortedOrdersResponse.from(sortedOrders);
    }

    private List<OrderItem> calculateOrderCartItems(List<Long> orderCartItemIds, List<CartItem> memberCartItems) {
        List<OrderItem> orderCartItems = memberCartItems.stream()
                .filter(memberCartItem -> orderCartItemIds.contains(memberCartItem.getId()))
                .map(memberCartItem -> new OrderItem(
                        memberCartItem.getId(),
                        memberCartItem.getProduct(),
                        memberCartItem.getQuantity()
                ))
                .collect(Collectors.toList());

        validateOrderCartItems(orderCartItemIds, orderCartItems);

        return orderCartItems;
    }

    private void validateOrderCartItems(List<Long> orderCartItemIds, List<OrderItem> orderCartItems) {
        if (orderCartItemIds.size() != orderCartItems.size()) {
            throw new NotFound();
        }
    }
}
