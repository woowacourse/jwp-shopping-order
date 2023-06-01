package cart.application;

import cart.domain.*;
import cart.dto.AllOrderResponse;
import cart.dto.OrderItemResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.repository.CartItemRepository;
import cart.repository.CouponRepository;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final CouponRepository couponRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    public OrderService(final CouponRepository couponRepository, final CartItemRepository cartItemRepository, final OrderRepository orderRepository) {
        this.couponRepository = couponRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
    }

    public Long order(final Member member, final OrderRequest request) {
        Coupon coupon = couponRepository.findById(request.getCouponId());

        List<CartItem> selectedCartItems = cartItemRepository.findByIds(request.getCartItemIds());

        for (CartItem selectedCartItem : selectedCartItems) {
            selectedCartItem.checkOwner(member);
        }

        List<OrderItem> orderItems = selectedCartItems.stream()
                .map(CartItem::toOrderItem)
                .collect(Collectors.toList());
        Order order = Order.of(orderItems, member, coupon);
        Long savedId = orderRepository.save(order);
        couponRepository.delete(coupon);
        return savedId;
    }

    public AllOrderResponse findAllOrderByMember(final Member member) {
        List<Order> allOrders = orderRepository.findAllByMember(member);
        List<OrderResponse> orderResponses = allOrders.stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());
        return new AllOrderResponse(orderResponses);
    }

    private OrderResponse convertToOrderResponse(final Order order) {
        List<OrderItemResponse> orderItemResponses = order.getOrderItems()
                .stream()
                .map(this::convertToOrderItemResponse)
                .collect(Collectors.toList());
        return new OrderResponse(
                order.getId(),
                orderItemResponses
        );
    }

    private OrderItemResponse convertToOrderItemResponse(final OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getProduct().getId(),
                orderItem.getProduct().getName(),
                orderItem.getProduct().getPrice(),
                orderItem.getProduct().getImageUrl(),
                orderItem.getQuantity()
        );
    }
}
