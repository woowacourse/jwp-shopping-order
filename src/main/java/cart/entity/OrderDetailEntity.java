package cart.entity;

public class OrderDetailEntity {
    private final Long id;
    private final Long ordersId;
    private final Long productId;
    private final String productName;
    private final String productImage;
    private final Integer productQuantity;
    private final Integer productPrice;

    public OrderDetailEntity(Long id, Long ordersId, Long productId, String productName, String productImage, Integer productQuantity, Integer productPrice) {
        this.id = id;
        this.ordersId = ordersId;
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;
    }

    public OrderDetailEntity(Long ordersId, Long productId, String productName, String productImage, Integer productQuantity, Integer productPrice) {
        this(null, ordersId, productId, productName, productImage, productQuantity, productPrice);
    }

    public Long getId() {
        return id;
    }

    public Long getOrdersId() {
        return ordersId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public Integer getProductPrice() {
        return productPrice;
    }
}
