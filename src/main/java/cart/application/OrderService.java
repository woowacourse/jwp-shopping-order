package cart.application;

import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.OrderBaseCouponGenerator;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.order.OrderItems;
import cart.dto.order.OrderDetailResponse;
import cart.dto.order.OrderRequest;
import cart.dto.order.OrderResponse;
import cart.repository.CartItemRepository;
import cart.repository.CouponRepository;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final CouponRepository couponRepository;
    private final OrderBaseCouponGenerator orderBaseCouponGenerator;

    public OrderService(OrderRepository orderRepository, CartItemRepository cartItemRepository,
                        CouponRepository couponRepository, OrderBaseCouponGenerator orderBaseCouponGenerator) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.couponRepository = couponRepository;
        this.orderBaseCouponGenerator = orderBaseCouponGenerator;
    }

    public void addOrder(Member member, OrderRequest orderRequest) {
        Order order = getOrderFromRequest(member, orderRequest);
        orderRepository.save(order);
        deleteItemsFromCart(orderRequest);
        useCoupon(member, order);
        issueAdditionalCoupon(member, order);
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

        return new Order(null, new OrderItems(orderItems), member, coupon, 3000, orderRequest.getTotalPrice(), LocalDateTime.now());
    }

    private void deleteItemsFromCart(OrderRequest orderRequest) {
        List<Long> cartItemIds = orderRequest.getCartItemIds();
        for (Long cartItemId : cartItemIds) {
            cartItemRepository.deleteById(cartItemId);
        }
    }

    private void useCoupon(Member member, Order order) {
        Coupon coupon = order.getCoupon();
        if (coupon != null) {
            couponRepository.deleteMemberCoupon(member.getId(), coupon.getId());
        }
    }

    private void issueAdditionalCoupon(Member member, Order order) {
        Optional<Coupon> nullableCoupon = orderBaseCouponGenerator.generate(order);
        if (nullableCoupon.isPresent()) {
            couponRepository.saveToMember(member, nullableCoupon.get());
        }
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
