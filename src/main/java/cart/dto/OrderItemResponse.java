package cart.dto;

public class OrderItemResponse {

    private final Long productId;
    private final String productName;
    private final Long quantity;
    private final Long price;
    private final String imageUrl;

    public OrderItemResponse(final Long productId, final String productName, final Long quantity, final Long price, final String imageUrl) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
