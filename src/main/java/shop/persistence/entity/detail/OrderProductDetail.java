package shop.persistence.entity.detail;

public class OrderProductDetail {
    private final Long id;
    private final Long orderId;
    private final Long productId;
    private final Integer orderedProductPrice;
    private final Integer quantity;
    private final String productName;
    private final Integer price;
    private final String imageUrl;

    public OrderProductDetail(Long id, Long orderId, Long productId, Integer orderedProductPrice,
                              Integer quantity, String productName, Integer price, String imageUrl) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.orderedProductPrice = orderedProductPrice;
        this.quantity = quantity;
        this.productName = productName;
        this.price = price;
        this.imageUrl = imageUrl;
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

    public Integer getOrderedProductPrice() {
        return orderedProductPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
