package cart.application;

import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.repository.CartItemRepository;
import cart.repository.CouponRepository;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final CouponRepository couponRepository;

    public OrderService(OrderRepository orderRepository, CartItemRepository cartItemRepository, CouponRepository couponRepository) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.couponRepository = couponRepository;
    }

    public void addOrder(Member member, OrderRequest orderRequest) {
        Order order = getOrderFromRequest(member, orderRequest);
        orderRepository.save(order);
        List<Long> cartItemIds = orderRequest.getCartItemIds();
        Coupon coupon = order.getCoupon();
        if (coupon != null) {
            couponRepository.deleteMemberCoupon(member.getId(), coupon.getId());
        }
        if (order.getFinalPrice() > 50000) {
            couponRepository.saveToMember(member, new Coupon(null, "5천원 할인 쿠폰", 5000, 0, LocalDateTime.now().plusDays(7)));
        }
        for (Long cartItemId : cartItemIds) {
            cartItemRepository.deleteById(cartItemId);
        }
    }

    private Order getOrderFromRequest(Member member, OrderRequest orderRequest) {
        List<OrderItem> orderItems = orderRequest.getCartItemIds().stream()
                .map(cartItemRepository::findById)
                .map(OrderItem::from)
                .collect(toList());
        Coupon coupon = null;
        Long couponId = orderRequest.getCouponId();
        if (couponId != null) {
            coupon = couponRepository.findById(couponId);
        }

        return new Order(null, orderItems, member, coupon, 3000, orderRequest.getTotalPrice(), LocalDateTime.now());
    }

    public List<OrderResponse> findByMember(Member member) {
        List<Order> orders = orderRepository.findByMemberId(member.getId());
        return orders.stream().map(OrderResponse::of).collect(toList());
    }

    public OrderDetailResponse findById(Long orderId) {
        Order order = orderRepository.findById(orderId);
        return OrderDetailResponse.of(order);
    }
}
