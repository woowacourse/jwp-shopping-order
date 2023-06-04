package cart.dto.request;

public class CartItemRequest {
    private Long productId;
    private int quantity;
    private String name;
    private int price;
    private String imageUrl;

    public CartItemRequest() {
    }

    public CartItemRequest(Long productId, int quantity, String name, int price, String imageUrl) {
        this.productId = productId;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
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
}
