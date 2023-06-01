package cart.dto;

import cart.domain.OrderItem;
import java.math.BigDecimal;

public class OrderItemResponse {

    private long id;
    private int quantity;
    private BigDecimal price;
    private String name;
    private String imageUrl;

    public OrderItemResponse(long id, int quantity, BigDecimal price, String name,
        String imageUrl) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public static OrderItemResponse fromOrderItem(OrderItem orderItem) {
        return new OrderItemResponse(
            orderItem.getId(),
            orderItem.getQuantityCount(),
            orderItem.getProductPrice(),
            orderItem.getProductName(),
            orderItem.getProductImage()
        );
    }

    public long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
