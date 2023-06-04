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
    private final CouponRepository couponRepository;
    private final OrderRepository orderRepository;

    public OrderService(CartItemRepository cartItemRepository, CouponRepository couponRepository,
                        OrderRepository orderRepository) {
        this.cartItemRepository = cartItemRepository;
        this.couponRepository = couponRepository;
        this.orderRepository = orderRepository;
    }

    public Long save(Member member, OrderRequest orderRequest) {
        validationSave(orderRequest);

        List<CartItem> cartItems = cartItemRepository.findAllByIdsAndMemberId(member, orderRequest.getSelectCartIds());

        Order order = new Order(
                member, cartItems,
                couponRepository.findAvailableCouponByIdAndMemberId(member, orderRequest.getCouponId()));

        return orderRepository.save(order);
    }

    private static void validationSave(OrderRequest orderRequest) {
        if (orderRequest.getSelectCartIds().isEmpty()) {
            throw new OrderException("주문 상품이 비어있습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<OrdersResponse> findAllByMemberId(Member member) {
        return orderRepository.findAllByMemberId(member).stream()
                .sorted(Comparator.comparing(Order::getId).reversed())
                .map(OrdersResponse::from)
                .collect(Collectors.toList());
    }

    public OrderResponse findByIdAndMemberId(Member member, Long orderId) {
        Order order = orderRepository.findByIdAndMemberId(member, orderId);

        return OrderResponse.of(order);
    }

    public void deleteById(Member member, Long orderId) {
        validationDelete(orderId);
        orderRepository.deleteById(member, orderId);
    }

    private void validationDelete(Long orderId) {
        if (orderRepository.checkConfirmStateById(orderId)) {
            throw new OrderException("주문 확정 주문은 취소할 수 없습니다.");
        }
    }

    public CouponConfirmResponse confirmById(Member member, Long orderId) {
        Coupon coupon = orderRepository.confirmById(orderId, member);

        return CouponConfirmResponse.from(CouponResponse.from(coupon));
    }
}
