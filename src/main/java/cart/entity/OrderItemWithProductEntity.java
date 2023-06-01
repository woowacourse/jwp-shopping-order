package cart.entity;

import java.time.LocalDateTime;

public class OrderItemWithProductEntity {

    private final long id;
    private final long orderId;
    private final long productId;
    private final String productName;
    private final int productPrice;
    private final String productImageUrl;
    private final int quantity;

    public OrderItemWithProductEntity(final long id, final long orderId, final long productId,
                                      final String productName, final int productPrice, final String productImageUrl,
                                      final int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public int getQuantity() {
        return quantity;
    }
}
