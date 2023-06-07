package cart.domain;

import java.util.Objects;

public class OrderItemEntity {

    private final Long id;
    private final Long orderId;
    private final Long productId;
    private final String productName;
    private final int productPrice;
    private final String productImageUrl;
    private final int quantity;

    public OrderItemEntity(
            final Long id,
            final Long orderId,
            final Long productId,
            final String productName,
            final int productPrice,
            final String productImageUrl,
            final int quantity
    ) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
        this.quantity = quantity;
    }

    public OrderItemEntity(
            final Long orderId,
            final Long productId,
            final String productName,
            final int productPrice,
            final String productImageUrl,
            final int quantity
    ) {
        this(null, orderId, productId, productName, productPrice, productImageUrl, quantity);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getProductId() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemEntity that = (OrderItemEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
