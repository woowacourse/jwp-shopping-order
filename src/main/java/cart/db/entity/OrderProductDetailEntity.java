package cart.db.entity;

public class OrderProductDetailEntity {

    private final Long id;
    private final Long orderId;
    private final Long productId;
    private final String productName;
    private final int productPrice;
    private final boolean productIsDeleted;
    private final String productImageUrl;
    private final int quantity;

    public OrderProductDetailEntity(
            final Long id,
            final Long orderId,
            final Long productId,
            final String productName,
            final int productPrice,
            final boolean productIsDeleted, final String productImageUrl,
            final int quantity
    ) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productIsDeleted = productIsDeleted;
        this.productImageUrl = productImageUrl;
        this.quantity = quantity;
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

    public boolean getProductIsDeleted() {
        return productIsDeleted;
    }

    public int getQuantity() {
        return quantity;
    }
}
