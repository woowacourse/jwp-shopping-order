package cart.dto.response;

import java.util.List;

public class OrderResponse {

    private Long orderId;
    private List<OrderItemResponse> items;
    private Long productPrice;
    private Long discountPrice;
    private Long deliveryFee;
    private Long totalPrice;

    public OrderResponse(final Long orderId, final List<OrderItemResponse> items, final Long productPrice,
        final Long discountPrice,
        final Long deliveryFee, final Long totalPrice) {
        this.orderId = orderId;
        this.items = items;
        this.productPrice = productPrice;
        this.discountPrice = discountPrice;
        this.deliveryFee = deliveryFee;
        this.totalPrice = totalPrice;
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public Long getProductPrice() {
        return productPrice;
    }

    public Long getDiscountPrice() {
        return discountPrice;
    }

    public Long getDeliveryFee() {
        return deliveryFee;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }
}
