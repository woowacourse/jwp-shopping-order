package cart.domain;

public class OrderItem {
    private final Long id;
    private final Long orderId;
    private final Long productId;
    private final String productName;
    private final String productImageUrl;
    private final int productPrice;
    private final int quantity;

    public OrderItem(
            Long orderId,
            Long productId,
            String productName,
            String productImageUrl,
            int productPrice,
            int quantity
    ) {
        this(null, orderId, productId, productName, productImageUrl, productPrice, quantity);
    }

    public OrderItem(
            Long id,
            Long orderId,
            Long productId,
            String productName,
            String productImageUrl,
            int productPrice,
            int quantity
    ) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.productImageUrl = productImageUrl;
        this.productPrice = productPrice;
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

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public int getQuantity() {
        return quantity;
    }
}
