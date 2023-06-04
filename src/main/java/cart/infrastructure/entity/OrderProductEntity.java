package cart.infrastructure.entity;

import java.math.BigDecimal;

public class OrderProductEntity {

    private final Long id;
    private final Long orderId;
    private final Long productId;
    private final int quantity;
    private final String productName;
    private final BigDecimal productPrice;
    private final String productImageUrl;

    public OrderProductEntity(Long orderId, Long productId, int quantity, String productName, BigDecimal productPrice,
                              String productImageUrl) {
        this(null, orderId, productId, quantity, productName, productPrice, productImageUrl);
    }

    public OrderProductEntity(Long id,
                              Long orderId,
                              Long productId,
                              int quantity,
                              String productName,
                              BigDecimal productPrice,
                              String productImageUrl
    ) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
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

    public int getQuantity() {
        return quantity;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }
}
