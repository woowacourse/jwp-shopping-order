package cart.dto.response;

import cart.domain.order.OrderPrice;
import java.util.List;

public class OrderResponse {

    private Long orderId;
    private List<OrderItemResponse> items;
    private Long productPrice;
    private Long discountPrice;
    private Long deliveryFee;
    private Long totalPrice;

    private OrderResponse(final Long orderId, final List<OrderItemResponse> items, final Long productPrice,
        final Long discountPrice,
        final Long deliveryFee, final Long totalPrice) {
        this.orderId = orderId;
        this.items = items;
        this.productPrice = productPrice;
        this.discountPrice = discountPrice;
        this.deliveryFee = deliveryFee;
        this.totalPrice = totalPrice;
    }

    public static OrderResponse of(final Long orderId, final List<OrderItemResponse> items, final Long productPrice,
        final Long discountPrice,
        final Long deliveryFee, final Long totalPrice) {
        return new OrderResponse(
            orderId,
            items,
            productPrice,
            discountPrice,
            deliveryFee,
            totalPrice
        );
    }

    public static OrderResponse of(final Long orderId, final List<OrderItemResponse> items,
        final OrderPrice orderPrice) {
        return new OrderResponse(
            orderId,
            items,
            orderPrice.getProductPrice(),
            orderPrice.getDiscountPrice(),
            orderPrice.getDeliveryFee(),
            orderPrice.getTotalPrice()
        );
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
