package cart.dto;

import cart.domain.OrderItem;

public class OrderItemDto {

    private final Long orderItemsId;
    private final String name;
    private final long price;
    private final String imageUrl;
    private final long quantity;

    public OrderItemDto(final Long orderItemsId, final String name, final long price, final String imageUrl, final long quantity) {
        this.orderItemsId = orderItemsId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public static OrderItemDto from(final OrderItem orderItem) {
        return new OrderItemDto(
                orderItem.getId(),
                orderItem.getName(),
                orderItem.getPrice(),
                orderItem.getImageUrl(),
                orderItem.getQuantity()
        );
    }

    public Long getOrderItemsId() {
        return orderItemsId;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public long getQuantity() {
        return quantity;
    }
}
