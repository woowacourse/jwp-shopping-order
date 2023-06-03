package cart.application;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.coupon.Coupon;
import cart.domain.repository.*;
import cart.dto.request.OrderRequest;
import cart.dto.response.*;
import cart.exception.OrderException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final CartItemRepository cartItemRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderCouponRepository orderCouponRepository;

    public OrderService(CartItemRepository cartItemRepository, MemberCouponRepository memberCouponRepository,
                        OrderRepository orderRepository, OrderProductRepository orderProductRepository,
                        OrderCouponRepository orderCouponRepository) {
        this.cartItemRepository = cartItemRepository;
        this.memberCouponRepository = memberCouponRepository;
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.orderCouponRepository = orderCouponRepository;
    }

    public Long save(Member member, OrderRequest orderRequest) {
        if (orderRequest.getSelectCartIds().isEmpty()) {
            throw new OrderException("주문 상품이 비어있습니다.");
        }

        List<CartItem> cartItems = cartItemRepository.findAllByIds(member, orderRequest.getSelectCartIds());

        Order order = new Order(
                member, cartItems,
                memberCouponRepository.findAvailableCouponByMember(member, orderRequest.getCouponId()));

        Long orderSavedId = orderRepository.saveOrder(order);
        cartItemRepository.deleteByMemberCartItemIds(member.getId(), cartItems);
        orderProductRepository.saveOrderProductsByOrderId(orderSavedId, order);
        memberCouponRepository.changeUserUsedCouponAvailability(order.getCoupon());
        orderCouponRepository.saveOrderCoupon(orderSavedId, order);
        return orderSavedId;
    }

    @Transactional(readOnly = true)
    public List<OrdersResponse> findAllByMemberId(Member member) {
        return orderRepository.findAllByMemberId(member).stream()
                .sorted(Comparator.comparing(Order::getId).reversed())
                .map(OrdersResponse::from)
                .collect(Collectors.toList());
    }

    public OrderResponse findByOrderId(Member member, Long orderId) {
        Order order = orderRepository.findByOrderId(member, orderId);

        return OrderResponse.of(order);
    }

    public void cancelOrder(Member member, Long orderId) {
        if (orderRepository.checkConfirmState(orderId)) {
            throw new OrderException("주문 확정 주문은 취소할 수 없습니다.");
        }
        Long memberCouponId = orderCouponRepository.deleteOrderCoupon(orderId);
        if (memberCouponId != null) {
            memberCouponRepository.changeUserUnUsedCouponAvailability(member, memberCouponId);
        }
        orderRepository.deleteOrder(orderId);
    }

    public CouponConfirmResponse confirmOrder(Member member, Long orderId) {
        orderRepository.confirmOrder(orderId, member);
        Coupon coupon = memberCouponRepository.publishBonusCoupon(orderId, member);

        return CouponConfirmResponse.from(CouponResponse.from(coupon));
    }
}
