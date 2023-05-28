package cart.entity;

public class OrderItemEntity {
    private final Long id;
    private final Long orderId;
    private final Long productId;
    private final String productName;
    private final int productPrice;
    private final int productQuantity;
    private final String productImageUrl;

    public OrderItemEntity(final Long id, final Long orderId, final Long productId, final String productName, final int productPrice, final int productQuantity, final String productImageUrl) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
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

    public String getProductName() {
        return productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }
}
