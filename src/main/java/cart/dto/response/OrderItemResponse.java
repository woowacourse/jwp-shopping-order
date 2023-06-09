package cart.dto.response;

import cart.domain.OrderItem;

public class OrderItemResponse {
    private final Long id;
    private final String name;
    private final String imageUrl;
    private final int price;
    private final int quantity;

    private OrderItemResponse(Long id, String name, String imageUrl, int price, int quantity) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderItemResponse of(OrderItem orderItem) {
        return new OrderItemResponse(
            orderItem.getId(),
            orderItem.getName(),
            orderItem.getImageUrl(),
            orderItem.getPrice(),
            orderItem.getQuantity()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}

