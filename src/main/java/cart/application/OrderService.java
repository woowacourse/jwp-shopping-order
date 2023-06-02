package cart.application;

import static java.util.stream.Collectors.toList;

import cart.domain.MemberCoupon;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Product;
import cart.dto.request.OrderCouponRequest;
import cart.dto.request.OrderItemRequest;
import cart.dto.response.OrderResponse;
import cart.repository.CouponRepository;
import cart.repository.OrderRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CouponRepository couponRepository;

    public OrderService(OrderRepository orderRepository, CouponRepository couponRepository) {
        this.orderRepository = orderRepository;
        this.couponRepository = couponRepository;
    }

    public Long order(List<OrderItemRequest> request, Long memberId) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest orderItemRequest : request) {
            Product product = orderItemRequest.getProduct().toDomain();
            List<Long> couponIds = orderItemRequest.getCoupons().stream()
                    .map(OrderCouponRequest::getId)
                    .collect(toList());

            List<MemberCoupon> memberCoupons = couponRepository.findAllByMemberCouponIds(couponIds);
            OrderItem orderItem = new OrderItem(product, orderItemRequest.getQuantity(), memberCoupons,
                    orderItemRequest.getTotalPrice());
            orderItems.add(orderItem);
        }
        return orderRepository.save(new Order(memberId, orderItems));
    }

    public OrderResponse findById(Long orderId) {
        Order order = orderRepository.findById(orderId);
        return OrderResponse.from(order);
    }
}
