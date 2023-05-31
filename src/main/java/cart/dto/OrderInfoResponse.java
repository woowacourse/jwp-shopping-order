package cart.dto;

public class OrderInfoResponse {

    private final Long productId;
    private final int price;
    private final String name;
    private final String imageUrl;
    private final int quantity;

    public OrderInfoResponse(Long productId, int price, String name, String imageUrl, int quantity) {
        this.productId = productId;
        this.price = price;
        this.name = name;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }
}
