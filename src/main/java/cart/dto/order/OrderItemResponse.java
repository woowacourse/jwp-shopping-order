package cart.dto.order;

import cart.domain.order.OrderItem;

public class OrderItemResponse {

    private Long productId;
    private String productName;
    private Long quantity;
    private Long price;
    private String imageUrl;

    public OrderItemResponse(Long productId, String productName, Long quantity, Long price, String imageUrl) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Long getProductId() {
        return productId;
    }

    public static OrderItemResponse of(OrderItem orderItem) {
        return new OrderItemResponse(orderItem.getProductId(), orderItem.getName(), orderItem.getQuantity(), orderItem.getPrice(), orderItem.getImageUrl());
    }

    public String getProductName() {
        return productName;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
