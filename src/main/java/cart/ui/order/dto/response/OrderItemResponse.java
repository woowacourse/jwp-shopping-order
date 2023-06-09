package cart.ui.order.dto.response;

public class OrderItemResponse {
    private Long id;
    private String productName;
    private Integer productPrice;
    private Integer paymentPrice;
    private String createdAt;
    private Integer productQuantity;
    private String imageUrl;

    public OrderItemResponse() {
    }

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
