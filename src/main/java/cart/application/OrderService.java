package cart.application;

import cart.domain.CartItems;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.coupon.Coupon;
import cart.domain.repository.CartItemRepository;
import cart.domain.repository.CouponRepository;
import cart.domain.repository.OrderRepository;
import cart.dto.MemberDto;
import cart.dto.request.OrderRequest;
import cart.dto.response.CouponConfirmResponse;
import cart.dto.response.CouponResponse;
import cart.dto.response.OrderResponse;
import cart.dto.response.OrdersResponse;
import cart.exception.CouponException;
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

    public Long save(MemberDto member, OrderRequest orderRequest) {
        validationSave(orderRequest);

        CartItems cartItems = new CartItems(cartItemRepository.findAllByIdsAndMemberId(orderRequest.getSelectedCartIds(), member.getId()));
        Coupon coupon = couponRepository.findAvailableCouponByIdAndMemberId(orderRequest.getCouponId(), member.getId());
        validateCoupon(coupon);
        Order order = new Order(new Member(member.getId(), member.getEmail(), null), cartItems, coupon);
        Order savedOrder = orderRepository.save(order);
        return savedOrder.getId();
    }

    private static void validationSave(OrderRequest orderRequest) {
        if (orderRequest.getSelectedCartIds().isEmpty()) {
            throw new OrderException("주문 상품이 비어있습니다.");
        }
    }

    private void validateCoupon(Coupon coupon) {
        if (coupon == null) {
            throw new CouponException("유효하지 않은 쿠폰입니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<OrdersResponse> findAllByMemberId(MemberDto member) {
        return orderRepository.findAllByMemberId(member.getId()).stream()
                .sorted(Comparator.comparing(Order::getId).reversed())
                .map(OrdersResponse::from)
                .collect(Collectors.toList());
    }

    public OrderResponse findByIdAndMemberId(MemberDto member, Long orderId) {
        Order order = orderRepository.findByIdAndMemberId(orderId, member.getId());

        return OrderResponse.of(order);
    }

    public void deleteById(Long orderId, MemberDto member) {
        validationDelete(orderId);
        orderRepository.deleteById(orderId, member.getId());
    }

    private void validationDelete(Long orderId) {
        if (orderRepository.existsConfirmStateById(orderId)) {
            throw new OrderException("주문 확정 주문은 취소할 수 없습니다.");
        }
    }

    public CouponConfirmResponse confirmById(Long orderId, MemberDto member) {
        Coupon coupon = orderRepository.confirmById(orderId, member.getId());

        return CouponConfirmResponse.from(CouponResponse.from(coupon));
    }
}
