package cart.dto;

import cart.domain.OrderItem;

public class OrderItemResponse {
    private final String name;
    private final int quantity;
    private final String imageUrl;
    private final int totalPrice;

    public OrderItemResponse(String name, int quantity, String imageUrl, int totalPrice) {
        this.name = name;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.totalPrice = totalPrice;
    }

    public static OrderItemResponse from(OrderItem orderItem) {
        return new OrderItemResponse(orderItem.getName(),
                orderItem.getQuantity(),
                orderItem.getImageUrl(),
                orderItem.getTotalPrice().getValue());
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
