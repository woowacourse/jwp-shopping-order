package cart.dto;

public class OrderProductResponse {

    private Long productId;
    private String productName;
    private int quantity;
    private int price;
    private String imageUrl;

    public OrderProductResponse(Long productId, String productName, int quantity, int price, String imageUrl) {
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

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
