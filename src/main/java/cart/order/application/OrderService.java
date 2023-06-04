package cart.order.application;

import cart.cart.Cart;
import cart.cartitem.CartItem;
import cart.controller.order.dto.OrderDetailResponse;
import cart.controller.order.dto.OrderRequest;
import cart.controller.order.dto.OrderResponse;
import cart.coupon.application.CouponService;
import cart.order.Order;
import cart.order.OrderCoupon;
import cart.order.OrderItem;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final CouponService couponService;
    private final OrderRepository orderRepository;

    public OrderService(CouponService couponService, OrderRepository orderRepository) {
        this.couponService = couponService;
        this.orderRepository = orderRepository;
    }

    public Long order(Long memberId, Cart cart, OrderRequest orderRequest) {
        // 1. OrderItems로 변환하기
        final ArrayList<OrderItem> orderItems = getOrderItems(cart, orderRequest);

        // 2. 쿠폰 정보를 OrderCoupons로 변환하기
        final var orderCoupons = getOrderCoupons(orderRequest.getCouponIds());

        // 3. Order로 만들어 저장하기
        final var order = new Order(
                orderItems,
                orderCoupons,
                cart.calculateFinalDeliveryPrice(),
                Timestamp.valueOf(LocalDateTime.now()),
                memberId
        );

        return orderRepository.save(order);

        // TODO: 주문한 만큼 장바구니에서 빼기

        // TODO: 주문할 때 사용한 쿠폰은 사용자에게서 빼기
    }

    private ArrayList<OrderItem> getOrderItems(Cart cart, OrderRequest orderRequest) {
        final var cartItems = cart.getCartItems();
        final var orderItems = new ArrayList<OrderItem>();
        for (CartItem cartItem : cartItems) {
            if (orderRequest.isCartItemOrdered(cartItem.getId())) {
                orderItems.add(OrderItem.from(cartItem));
            }
        }
        return orderItems;
    }

    private List<OrderCoupon> getOrderCoupons(List<Long> couponIds) {
        return couponService.getOrderCoupons(couponIds);
    }

    public List<OrderResponse> findOrderHistories(Long memberId) {
        final var orders = orderRepository.findAllByMemberId(memberId);
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public OrderDetailResponse findOrderHistory(Long orderId) {
        final var order = orderRepository.findById(orderId);
        return OrderDetailResponse.from(order);
    }
}
