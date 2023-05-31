package cart.dto;

public class OrderProductResponse {
    private final Long productId;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final int quantity;

    public OrderProductResponse(final Long productId,
                                final String name,
                                final int price,
                                final String imageUrl,
                                final int quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }
}
