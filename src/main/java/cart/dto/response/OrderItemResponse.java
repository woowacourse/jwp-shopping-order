package cart.dto.response;

import cart.domain.order.OrderItem;

public class OrderItemResponse {

    private final Long id;
    private final int quantity;
    private final int price;
    private final String name;
    private final String imageUrl;

    public OrderItemResponse(
            final Long id,
            final int quantity,
            final int price,
            final String name,
            final String imageUrl
    ) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public static OrderItemResponse from(final OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getQuantity().quantity(),
                orderItem.getProduct().getPrice().price(),
                orderItem.getProduct().getName().name(),
                orderItem.getProduct().getImage().imageUrl()
        );
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
