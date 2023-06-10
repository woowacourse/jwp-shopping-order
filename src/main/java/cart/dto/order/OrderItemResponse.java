package cart.dto.order;

import cart.domain.order.OrderItem;
import cart.dto.coupon.CouponResponse;
import cart.dto.product.ProductResponse;

import java.util.List;
import java.util.stream.Collectors;

public class OrderItemResponse {

    private Long orderItemId;
    private ProductResponse product;
    private int quantity;
    private List<CouponResponse> coupons;
    private int total;

    private OrderItemResponse() {
    }

    public OrderItemResponse(
            final Long orderItemId,
            final ProductResponse product,
            final int quantity,
            final List<CouponResponse> coupons,
            final int total
    ) {
        this.orderItemId = orderItemId;
        this.product = product;
        this.quantity = quantity;
        this.coupons = coupons;
        this.total = total;
    }

    public static OrderItemResponse from(OrderItem orderItem) {
        List<CouponResponse> couponIds = orderItem.getCoupons()
                .stream()
                .map(CouponResponse::from)
                .collect(Collectors.toList());

        return new OrderItemResponse(
                orderItem.getId(),
                ProductResponse.of(orderItem.getProduct()),
                orderItem.getQuantity(),
                couponIds,
                orderItem.getTotal()
        );
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public List<CouponResponse> getCoupons() {
        return coupons;
    }

    public int getTotal() {
        return total;
    }
}
