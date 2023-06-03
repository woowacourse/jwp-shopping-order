package cart.application;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import cart.domain.MemberCoupon;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Product;
import cart.dto.request.OrderCouponRequest;
import cart.dto.request.OrderItemRequest;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderResponse;
import cart.repository.CartItemRepository;
import cart.repository.CouponRepository;
import cart.repository.OrderRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CouponRepository couponRepository;
    private final CartItemRepository cartItemRepository;


    public OrderService(OrderRepository orderRepository, CouponRepository couponRepository,
                        CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.couponRepository = couponRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public Long order(OrderRequest request, Long memberId) {
        List<Long> couponIds = request.getOrderItemRequests().stream()
                .flatMap(orderItemRequest -> orderItemRequest.getCoupons().stream())
                .map(OrderCouponRequest::getCouponId)
                .collect(toList());
        List<MemberCoupon> memberCoupons = couponRepository.findAllByMemberCouponIds(couponIds);
        Map<Long, MemberCoupon> memberCouponsById = memberCoupons.stream()
                .collect(toMap(MemberCoupon::getId, Function.identity()));

        List<OrderItem> orderItems = getOrderItems(request, memberCouponsById);
        couponRepository.useCoupons(memberCoupons);

        int totalPrice = orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
        return orderRepository.save(new Order(memberId, orderItems, totalPrice));
    }

    private List<OrderItem> getOrderItems(OrderRequest request, Map<Long, MemberCoupon> memberCouponsById) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest orderItemRequest : request.getOrderItemRequests()) {
            Product product = orderItemRequest.getProduct().toDomain();
            List<MemberCoupon> memberCoupons = new ArrayList<>();
            for (OrderCouponRequest coupon : orderItemRequest.getCoupons()) {
                memberCoupons.add(memberCouponsById.get(coupon.getCouponId()));
            }

            int totalPrice = applyCoupon(orderItemRequest, product, memberCoupons);

            OrderItem orderItem = new OrderItem(product, orderItemRequest.getQuantity(), memberCoupons, totalPrice);
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    private int applyCoupon(OrderItemRequest orderItemRequest, Product product, List<MemberCoupon> memberCoupons) {
        int price = product.getPrice() * orderItemRequest.getQuantity();
        for (MemberCoupon memberCoupon : memberCoupons) {
            memberCoupon.getCoupon().apply(price);
        }
        return price;
    }

    public OrderResponse findById(Long orderId) {
        Order order = orderRepository.findById(orderId);
        return OrderResponse.from(order);
    }

    public List<OrderResponse> findAllByMemberId(Long memberId) {
        List<Order> orders = orderRepository.findAllByMemberId(memberId);
        return orders.stream()
                .map(OrderResponse::from)
                .collect(toList());
    }
}
