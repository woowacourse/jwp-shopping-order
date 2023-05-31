package cart.dto;

import cart.domain.Coupon;
import cart.domain.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderItemResponse {

    private final Long orderId;
    private final ProductResponse product;
    private final int quantity;
    private final List<Long> couponId;
    private final int total;

    public OrderItemResponse(
            final Long orderId,
            final ProductResponse product,
            final int quantity,
            final List<Long> couponId,
            final int total
    ) {
        this.orderId = orderId;
        this.product = product;
        this.quantity = quantity;
        this.couponId = couponId;
        this.total = total;
    }

    public static OrderItemResponse from(OrderItem orderItem) {
        List<Long> couponIds = orderItem.getCoupons()
                .stream()
                .map(Coupon::getId)
                .collect(Collectors.toList());

        return new OrderItemResponse(
                orderItem.getId(),
                ProductResponse.of(orderItem.getProduct()),
                orderItem.getQuantity(),
                couponIds,
                orderItem.getTotal()
        );
    }

    public Long getOrderId() {
        return orderId;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public List<Long> getCouponIds() {
        return couponId;
    }

    public int getTotal() {
        return total;
    }
}
