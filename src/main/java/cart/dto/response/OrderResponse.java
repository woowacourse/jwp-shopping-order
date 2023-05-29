package cart.dto.response;

import java.util.List;

public class OrderResponse {

    private Long orderId;
    private List<OrderItemResponse> items;
    private Long totalPrice;
    private Long discountedPrice;

    public OrderResponse(final Long orderId, final List<OrderItemResponse> items, final Long totalPrice,
        final Long discountedPrice) {
        this.orderId = orderId;
        this.items = items;
        this.totalPrice = totalPrice;
        this.discountedPrice = discountedPrice;
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public Long getDiscountedPrice() {
        return discountedPrice;
    }
}
