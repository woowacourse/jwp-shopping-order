package cart.ui.order.dto;

public class OrderItemResponse {
    private final Long id;
    private final String productName;
    private final Integer productPrice;
    private final Integer paymentPrice;
    private final String createdAt;
    private final Integer productQuantity;
    private final String imageUrl;

    public OrderItemResponse(
            final Long id,
            final String productName,
            final Integer productPrice,
            final Integer paymentPrice,
            final String createdAt,
            final Integer productQuantity,
            final String imageUrl
    ) {
        this.id = id;
        this.productName = productName;
        this.productPrice = productPrice;
        this.paymentPrice = paymentPrice;
        this.createdAt = createdAt;
        this.productQuantity = productQuantity;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getProductPrice() {
        return productPrice;
    }

    public Integer getPaymentPrice() {
        return paymentPrice;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}


