package cart.application.service.order;

import cart.domain.order.OrderItem;

import java.util.List;

public class OrderItemDto {
    private final Long id;
    private final String productName;
    private final int productPrice;
    private final int paymentPrice;
    private final String createdAt;
    private final int productQuantity;
    private final String imageUrl;

    public OrderItemDto(final Long id, final String productName, final int productPrice, final int paymentPrice, final String createdAt, final int productQuantity, final String imageUrl) {
        this.id = id;
        this.productName = productName;
        this.productPrice = productPrice;
        this.paymentPrice = paymentPrice;
        this.createdAt = createdAt;
        this.productQuantity = productQuantity;
        this.imageUrl = imageUrl;
    }

    public static OrderItemDto of(final List<OrderItem> orderItems) {
        return null;
    }

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public int getPaymentPrice() {
        return paymentPrice;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
