package cart.dto;

import cart.domain.Order.OrderItem;

public class OrderItemResponse {

    private final Long id;
    private final int quantity;
    private final String name;
    private final int price;
    private final String imageUrl;

    public OrderItemResponse(Long id, int quantity, String name, int price, String imageUrl) {
        this.id = id;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static OrderItemResponse of(OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getProduct().getId(),
                orderItem.getQuantity(),
                orderItem.getProduct().getName().name(),
                orderItem.getPrice(),
                orderItem.getProduct().getImageUrl().imageUrl()
        );


    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}