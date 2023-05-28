package cart.dto;

import cart.domain.Orders;
import java.util.List;
import java.util.stream.Collectors;

public class OrdersDto {

    private final Long id;
    private final Long totalItemsPrice;
    private final Long discountPrice;
    private final Long deliveryFee;
    private final List<OrderItemDto> orderItems;

    public OrdersDto(final Orders orders) {
        this.id = orders.getId();
        this.totalItemsPrice = orders.getTotalItemPrice();
        this.discountPrice = 0L;
        this.deliveryFee = orders.getDeliveryFee();
        this.orderItems = orders.getOrderItems().stream()
                .map(it -> new OrderItemDto(it.getId(), it.getName(), it.getPrice(), it.getImageUrl(),
                        it.getQuantity()))
                .collect(Collectors.toList());
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

    public List<OrderItemDto> getOrderItems() {
        return orderItems;
    }
}
