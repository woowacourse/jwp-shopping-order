package cart.dto.response;

public class OrderItemResponse {

    private final Long productId;
    private final String name;
    private final int price;
    private final int quantity;
    private final String imageUrl;

    public OrderItemResponse(final Long productId, final String name, final int price, final int quantity, final String imageUrl) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
