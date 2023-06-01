package cart.domain;

public class OrderDetail {
    private final Long orderId;
    private final Long productId;
    private final String productName;
    private final String productImage;
    private final Integer productQuantity;
    private final Integer productPrice;

    public OrderDetail(Long orderId, Long productId, String productName, String productImage, Integer productQuantity, Integer productPrice) {
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;
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
