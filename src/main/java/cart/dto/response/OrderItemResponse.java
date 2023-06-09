package cart.dto.response;

import cart.domain.OrderItem;
import java.util.List;
import java.util.stream.Collectors;

public class OrderItemResponse {

    private Long orderItemId;
    private OrderProductResponse product;
    private Integer quantity;
    private List<OrderCouponResponse> coupons;
    private Integer total;

    public OrderItemResponse() {
    }

    public OrderItemResponse(Long orderItemId, OrderProductResponse product, Integer quantity, List<OrderCouponResponse> coupons,
                             Integer total) {
        this.orderItemId = orderItemId;
        this.product = product;
        this.quantity = quantity;
        this.coupons = coupons;
        this.total = total;
    }

    public static OrderItemResponse from(OrderItem orderItem) {
        List<OrderCouponResponse> coupons = orderItem.getCoupons().stream()
                .map(OrderCouponResponse::from)
                .collect(Collectors.toList());
        return new OrderItemResponse(orderItem.getId(), OrderProductResponse.from(orderItem.getProduct()),
                orderItem.getQuantity(), coupons,
                orderItem.getTotalPrice());
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public OrderProductResponse getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public List<OrderCouponResponse> getCoupons() {
        return coupons;
    }

    public Integer getTotal() {
        return total;
    }
}
