package cart.application;

import cart.dao.OrderItemDao;
import cart.dao.OrdersDao;
import cart.domain.CartItem;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Product;
import cart.dto.CartItemDto;
import cart.dto.OrderCreateResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.dto.OrderResponses;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final CouponService couponService;
    private final OrdersDao ordersDao;
    private final OrderItemDao orderItemDao;

    public OrderService(CouponService couponService, OrdersDao ordersDao, OrderItemDao orderItemDao) {
        this.couponService = couponService;
        this.ordersDao = ordersDao;
        this.orderItemDao = orderItemDao;
    }

    public OrderCreateResponse add(Member member, OrderRequest orderRequest) {
        int originalPrice = calculateOriginalPrice(orderRequest);
        Coupon coupon = couponService.findById(getCouponId(orderRequest));
        int actualPrice = coupon.calculateActualPrice(originalPrice);
        Order orderToAdd = new Order(member.getId(), originalPrice, actualPrice, orderRequest.getDeliveryFee());
        long orderId = ordersDao.save(orderToAdd);
        addOrderItems(orderId, orderRequest);
        return new OrderCreateResponse(orderId);
    }

    private Long getCouponId(OrderRequest orderRequest) {
        if (orderRequest.getCouponIds().size() == 1) {
            return orderRequest.getCouponIds().get(0);
        }
        throw new IllegalArgumentException(String.format(
                "쿠폰의 개수가 %s개 입력되었습니다. 현재 정책 상 쿠폰은 1개만 입력할 수 있습니다.",
                orderRequest.getCouponIds().size()
        ));
    }

    private int calculateOriginalPrice(OrderRequest orderRequest) {
        return orderRequest.getCartItems().stream()
                .mapToInt(cartItemDto -> cartItemDto.getProduct().getPrice())
                .sum();
    }

    private void addOrderItems(long orderId, OrderRequest orderRequest) {
        for (CartItemDto cartItemDto : orderRequest.getCartItems()) {
            OrderItem orderItemToAdd = new OrderItem(
                    orderId,
                    cartItemDto.getProduct().getId(),
                    cartItemDto.getProduct().getName(),
                    cartItemDto.getProduct().getImageUrl(),
                    cartItemDto.getProduct().getPrice(),
                    cartItemDto.getQuantity()
            );
            orderItemDao.save(orderItemToAdd);
        }
    }

    public OrderResponses findAll(Member member) {
        List<Order> orders = ordersDao.findByMemberId(member.getId());
        return new OrderResponses(orders.stream()
                .map(order -> mapOrderToResponse(member, order))
                .collect(Collectors.toList())
        );
    }

    public OrderResponse findById(Member member, Long orderId) {
        return mapOrderToResponse(member, ordersDao.findById(orderId));
    }

    private OrderResponse mapOrderToResponse(Member member, Order order) {
        List<OrderItem> orderItems = orderItemDao.findByOrderId(order.getId());
        return new OrderResponse(
                order.getId(),
                order.getOriginalPrice(),
                order.getActualPrice(),
                order.getDeliveryFee(),
                orderItems.stream()
                        .map(orderItem -> mapOrderItemToCartItemDto(member, orderItem))
                        .collect(Collectors.toList())
        );
    }

    private CartItemDto mapOrderItemToCartItemDto(Member member, OrderItem orderItem) {
        return CartItemDto.of(new CartItem(
                orderItem.getId(),
                orderItem.getQuantity(),
                new Product(
                        orderItem.getProductId(),
                        orderItem.getProductName(),
                        orderItem.getProductPrice(),
                        orderItem.getProductImageUrl()),
                member)
        );
    }
}
