package cart.application;

import cart.domain.*;
import cart.dto.*;
import cart.repository.CartItemRepository;
import cart.repository.CouponRepository;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Long createOrder(final Member member, final OrderRequest request) {
        Coupon coupon = findCouponIfExist(request.getCouponId());
        CartItems cartItems = new CartItems(findCartItemsRequest(request));
        CartItems selectedCartItems = new CartItems(convertToCartItems(member, request));

        cartItems.checkStatus(selectedCartItems, member);
        Order order = cartItems.order(member, coupon);

        if (coupon != Coupon.EMPTY_COUPON) {
            couponRepository.update(coupon);
        }
        cartItemRepository.deleteAll(cartItems.getCartItems());
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public AllOrderResponse findAllOrderByMember(final Member member) {
        List<Order> allOrders = orderRepository.findAllByMember(member);

        for (Order order : allOrders) {
            order.checkOwner(member);
        }

        return convertToAllOrderResponse(allOrders);
    }

    private AllOrderResponse convertToAllOrderResponse(final List<Order> allOrders) {
        List<OrderResponse> orderResponses = allOrders.stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());
        return new AllOrderResponse(orderResponses);
    }

    @Transactional(readOnly = true)
    public OrderDetailResponse findOrderByIdAndMember(final Long id, final Member member) {
        Order order = orderRepository.findByIdAndMember(id, member);

        order.checkOwner(member);

        return convertToOrderDetailResponse(order);
    }

    private Coupon findCouponIfExist(final Long couponId) {
        if (couponId== null) {
            return Coupon.EMPTY_COUPON;
        }
        return couponRepository.findById(couponId);
    }

    private List<CartItem>  findCartItemsRequest(final OrderRequest request) {
        List<Long> currentCartIds = request.getProducts().stream()
                .map(OrderCartItemRequest::getCartItemId)
                .collect(Collectors.toList());
        return cartItemRepository.findByIds(currentCartIds);
    }

    private List<CartItem> convertToCartItems(final Member member, final OrderRequest request) {
        return request.getProducts().stream()
                .map(orderCartItemRequest -> convertToCartItem(orderCartItemRequest, member))
                .collect(Collectors.toList());
    }

    // TODO: 6/1/23 request에 id 받아와야 될지 고민해보기
    private CartItem convertToCartItem(final OrderCartItemRequest orderCartItemRequest, final Member member) {
        return new CartItem(
                orderCartItemRequest.getCartItemId(),
                orderCartItemRequest.getQuantity(),
                new Product(null, orderCartItemRequest.getName(), orderCartItemRequest.getPrice(), orderCartItemRequest.getImageUrl()),
                member
        );
    }

    private OrderDetailResponse convertToOrderDetailResponse(final Order order) {
        List<OrderItemResponse> orderItemResponses = order.getOrderItems()
                .stream()
                .map(this::convertToOrderItemResponse)
                .collect(Collectors.toList());
        return new OrderDetailResponse(
                order.getId(),
                orderItemResponses,
                order.calculateTotalPrice(),
                order.calculateDiscountPrice(),
                order.getShippingFee().getCharge()
        );
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
