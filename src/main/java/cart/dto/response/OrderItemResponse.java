package cart.dto.response;

import cart.domain.OrderItem;
import java.util.List;
import java.util.stream.Collectors;

public class OrderItemResponse {

    private Long id;
    private OrderProductResponse product;
    private Integer quantity;
    private List<OrderCouponResponse> coupons;
    private Integer totalPrice;

    public OrderItemResponse() {
    }

    public OrderItemResponse(Long id, OrderProductResponse product, Integer quantity, List<OrderCouponResponse> coupons,
                             Integer totalPrice) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.coupons = coupons;
        this.totalPrice = totalPrice;
    }

    public static OrderItemResponse from(OrderItem orderItem) {
        List<OrderCouponResponse> coupons = orderItem.getCoupons().stream()
                .map(OrderCouponResponse::from)
                .collect(Collectors.toList());
        return new OrderItemResponse(orderItem.getId(), OrderProductResponse.from(orderItem.getProduct()),
                orderItem.getQuantity(), coupons,
                orderItem.getTotalPrice());
    }

    public Long getId() {
        return id;
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

    public Integer getTotalPrice() {
        return totalPrice;
    }
}
