package cart.dto;

import cart.domain.cart.Order;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private final Long id;
    private final Long totalItemsPrice;
    private final Long discountPrice;
    private final Long deliveryFee;
    private final List<ItemDto> orderItems;

    public OrderResponse(
            final Long id,
            final Long totalItemsPrice,
            final Long discountPrice,
            final Long deliveryFee,
            final List<ItemDto> orderItems
    ) {
        this.id = id;
        this.totalItemsPrice = totalItemsPrice;
        this.discountPrice = discountPrice;
        this.deliveryFee = deliveryFee;
        this.orderItems = orderItems;
    }

    public static OrderResponse from(final Order order) {
        final List<ItemDto> items = order.getItems().stream()
                .map(ItemDto::from)
                .collect(Collectors.toList());
        return new OrderResponse(
                order.getId(),
                order.calculateTotalPrice().getLongValue(),
                order.calculateDiscountPrice().getLongValue(),
                order.calculateDeliveryFee().getLongValue(),
                items
        );
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "id=" + id +
                ", totalItemsPrice=" + totalItemsPrice +
                ", discountPrice=" + discountPrice +
                ", deliveryFee=" + deliveryFee +
                ", orderItems=" + orderItems +
                '}';
    }

    public Long getId() {
        return id;
    }

    public Long getTotalItemsPrice() {
        return totalItemsPrice;
    }

    public Long getDiscountPrice() {
        return discountPrice;
    }

    public Long getDeliveryFee() {
        return deliveryFee;
    }

    public List<ItemDto> getOrderItems() {
        return orderItems;
    }
}
