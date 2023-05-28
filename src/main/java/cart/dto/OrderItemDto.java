package cart.dto;

public class OrderItemDto {

    private final Long orderItemId;
    private final String name;
    private final Long price;
    private final String imageUrl;
    private final Integer quantity;

    public OrderItemDto(final Long orderItemId, final String name, final Long price, final String imageUrl,
                        final Integer quantity) {
        this.orderItemId = orderItemId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
