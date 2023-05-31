package cart.entity;

public class OrderItemEntity {

    private Long id;
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private String productName;
    private Integer productPrice;
    private String productImageUrl;

    public OrderItemEntity(Long orderId, Long productId, Integer quantity, String productName, Integer productPrice,
                           String productImageUrl) {
        this(null, orderId, productId, quantity, productName, productPrice, productImageUrl);
    }

    public OrderItemEntity(Long id, Long orderId, Long productId, Integer quantity, String productName,
                           Integer productPrice, String productImageUrl) {
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

    public Integer getQuantity() {
        return quantity;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getProductPrice() {
        return productPrice;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }
}
